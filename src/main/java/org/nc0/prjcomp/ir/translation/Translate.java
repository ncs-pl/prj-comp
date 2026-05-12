// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ir.translation;

import org.nc0.prjcomp.ast.*;
import org.nc0.prjcomp.ir.Frame;
import org.nc0.prjcomp.ir.Register;
import org.nc0.prjcomp.ir.Type;
import org.nc0.prjcomp.ir.com.*;
import org.nc0.prjcomp.ir.expr.Binary;
import org.nc0.prjcomp.ir.expr.Int;
import org.nc0.prjcomp.ir.expr.ReadReg;
import org.nc0.prjcomp.ir.expr.Unary;
import org.nc0.prjcomp.semantic.MethodSig;
import org.nc0.prjcomp.semantic.SymbolTable;
import org.nc0.prjcomp.semantic.TypeChecker;
import org.nc0.prjcomp.support.Errors;
import org.nc0.prjcomp.support.ListTools;
import org.nc0.prjcomp.support.Maps;
import org.nc0.prjcomp.support.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Translate {
    private static final TypeConverter typeConverter = new TypeConverter();

    private static Type ofType(org.nc0.prjcomp.ast.Type type) {
        return type.accept(typeConverter);
    }

    public static Pair<Label, List<Pair<Frame, List<Command>>>> run(SymbolTable symbolTable, Program program) {
        var translator = new TranslationVisitor(symbolTable);
        program.accept(translator);
        Errors errors = translator.errors;
        if (translator.mainLabel == null) {
            errors.add("pas de fonction main.");
        }
        if (errors.hasErrors()) {
            System.out.println("Erreurs de traduction en code intermédiaire :");
            errors.print();
            System.out.println("Sortie avant production assembleur");
            System.exit(1);
        }
        return new Pair<>(translator.mainLabel, translator.fragments);
    }

    private static class TypeConverter extends BaseVisitor<Type> {
        //pour convertir les types de l’AST en types de l’IR
        public TypeConverter() {
            super(null);
        }

        @Override
        public Type visit(TypePrim type) {
            return switch (type.getPrim()) {
                case TypePrim.Prim.INT -> Type.INT;
                case TypePrim.Prim.BOOL -> Type.BYTE;
                default -> throw new Error("Traduction IR : type primitif non géré : " + type.getPrim().toString());
            };
        }
    }

    private static class TranslationVisitor extends BaseVisitor<Result> {
        // La table des symboles et le typeChecker, utiles pour au moins deux éléments :
        // - Savoir à quel bloc se réfère un nom de variable (pile des blocs)
        // - Savoir quel est le type pour les registres associés aux variables.
        private final SymbolTable symbolTable;
        private final TypeChecker typeChecker;
        private final Map<String, Frame> frames; // Chaque nom de méthode est associé à une frame (construit par le FramesBuilder)
        private final Map<Block, Map<String, Register>> varToReg; // Dans chaque bloc, chaque variable est associée à un registre.
        private final List<Pair<Frame, List<Command>>> fragments; // Chaque méthode sera enregistrée dans une liste de paires, avec son frame et son code.
        public Errors errors;
        private Frame currentFrame; // Le frame courant, auquel on ajoute en particulier les registres temporaires nécessaires au calcul de la fonction (qui seront empilés lors de l’appel).
        private Label mainLabel; //Un label spécifique, correspondant au point d’entrée de la fonction main.

        public TranslationVisitor(SymbolTable symbolTable) {
            super(null);
            this.symbolTable = symbolTable;
            this.varToReg = new HashMap<>();
            this.typeChecker = new TypeChecker(symbolTable);
            this.fragments = new LinkedList<>();
            this.frames = new HashMap<>();
            this.currentFrame = null;
            this.mainLabel = null;
            this.errors = new Errors();
        }

        @Override
        public Result visit(MethodDecl methodDeclaration) {
            currentFrame = frames.get(methodDeclaration.getId().getName());
            Result bodyResult = methodDeclaration.getBlock().accept(this);
            fragments.add(new Pair<>(currentFrame, bodyResult.getCode()));
            return null;
        }

        @Override
        public Result visit(Program program) {
            program.accept(typeChecker);
            FramesBuilder framesBuilder = new FramesBuilder();
            program.accept(framesBuilder);
            program.getListMethodDecl().forEach(method -> method.accept(this));
            return null;
        }

        @Override
        public Result visit(StatAff affectationStatement) {
            assert currentFrame != null;
            Result expressionResult = affectationStatement.getExpression().accept(this);
            Register register = this.registerLookup(affectationStatement.getId().getName());
            var code = new LinkedList<>(expressionResult.getCode());
            code.add(new WriteReg(register, expressionResult.getExp()));
            return new Result(code);
        }

        private Register registerLookup(String name) {
            Register reg;
            for (Block block : typeChecker.getVisitedBlocks().getStack()) {
                Map<String, Register> map = varToReg.get(block);

                reg = map.get(name);
                //System.out.println("lookup "+block+": "+name);x
                if (reg != null)
                    return reg;
            }
            Maps.print(varToReg);
            errors.add("Erreur interne, pas de registre associé à la variable " + name);
            return null;
        }

        @Override
        public Result visit(StatIf ifStatement) {
            assert currentFrame != null;
            Result conditionResult = ifStatement.getExpression().accept(this);
            Result thenResult = ifStatement.getIfBlock().accept(this);
            Result elseResult = ifStatement.getElseBlock().accept(this);
            var labelThen = Label.fresh();
            var labelElse = Label.fresh();
            var labelEnd = Label.fresh();
            var code = new LinkedList<>(conditionResult.getCode());
            code.add(new CJump(conditionResult.getExp(), labelThen, labelElse));
            code.add(labelThen);
            code.addAll(thenResult.getCode());
            code.add(new Jump(labelEnd));
            code.add(labelThen);
            code.addAll(elseResult.getCode());
            code.add(new Jump(labelEnd));
            code.add(labelEnd);
            return new Result(code);
        }

        @Override
        public Result visit(StatList statements) {
            assert currentFrame != null;
            LinkedList<Command> code = statements.getStatList().stream().map(statement -> statement.accept(this)).flatMap(result -> result.getCode().stream()).collect(Collectors.toCollection(LinkedList::new));
            return new Result(code);
        }

        @Override
        public Result visit(StatPrint printStatement) {
            assert currentFrame != null;
            Result expressionResult = printStatement.getExpression().accept(this);
            Label entryPoint = Label.named("print_entry");
            Label exitPoint = Label.named("print_exit");
            var parameters = new ArrayList<Register>();
            parameters.add(new Register(Type.INT)); // input
            var output = new Register(Type.INT); // NOTE(nico): all functions must return something...
            Frame frame = new Frame(entryPoint, exitPoint, parameters, output);
            List<org.nc0.prjcomp.ir.expr.Expression> args = ListTools.mklist(expressionResult.getExp());
            org.nc0.prjcomp.ast.Type type = new TypePrim(null, TypePrim.Prim.INT);
            return makeFunCall(type, frame, args, expressionResult.getCode());
        }

        @Override
        public Result visit(StatWhile whileStatement) {
            Result conditionResult = whileStatement.getExpression().accept(this);
            Result bodyResult = whileStatement.getBlock().accept(this);
            Label conditionLabel = Label.fresh();
            Label bodyLabel = Label.fresh();
            Label endLabel = Label.fresh();
            var code = new LinkedList<Command>();
            code.add(conditionLabel);
            code.addAll(conditionResult.getCode());
            code.add(new CJump(conditionResult.getExp(), bodyLabel, endLabel));
            code.add(bodyLabel);
            code.addAll(bodyResult.getCode());
            code.add(new Jump(bodyLabel));
            code.add(endLabel);
            return new Result(code);
        }

        @Override
        public Result visit(StatVarDecl declarationStatement) {
            assert currentFrame != null;
            var register = new Register(Translate.ofType(declarationStatement.getType()));
            Block block = typeChecker.getVisitedBlocks().current();
            varToReg.computeIfAbsent(block, _ -> new HashMap<>());
            varToReg.get(block).put(declarationStatement.getId().getName(), register);
            currentFrame.addLocal(register);
            return new Result(new LinkedList<>());
        }

        @Override
        public Result visit(StatReturn returnStatement) {
            assert currentFrame != null;
            Result expressionResult = returnStatement.getExpression().accept(this);
            var code = new LinkedList<>(expressionResult.getCode());
            code.add(new WriteReg(currentFrame.getResult(), expressionResult.getExp()));
            code.add(new Jump(currentFrame.getExitPoint()));
            return new Result(code);
        }

        @Override
        public Result visit(Block block) {
            varToReg.computeIfAbsent(block, _ -> new HashMap<>());
            typeChecker.getVisitedBlocks().enter(block);
            Statement statement = block.getStatement();
            Result result = statement.accept(this);
            var code = new LinkedList<>(result.getCode());
            typeChecker.getVisitedBlocks().exit();
            return new Result(code);
        }

        @Override
        public Result visit(ExpBin binaryExpression) {
            Result leftResult = binaryExpression.getLeftExp().accept(this);
            Result rightResult = binaryExpression.getRightExp().accept(this);
            var binary = new Binary(leftResult.getExp(), rightResult.getExp(), binaryExpression.getOp());
            var code = new LinkedList<>(leftResult.getCode());
            code.addAll(rightResult.getCode());
            return new Result(binary, code);
        }

        @Override
        public Result visit(ExpCallMethod callExpression) {
            String functionName = callExpression.getMethod().getName();
            List<Result> results = callExpression.getArgs().stream().map(expression -> expression.accept(this)).toList();
            List<Command> code = results.stream().flatMap(result -> result.getCode().stream()).collect(Collectors.toCollection(LinkedList::new));
            List<org.nc0.prjcomp.ir.expr.Expression> arguments = results.stream().map(Result::getExp).collect(Collectors.toCollection(LinkedList::new));
            MethodSig signature = symbolTable.methodLookup(functionName);
            if (signature == null) {
                errors.add("Erreur interne, " + functionName + " pas dans la table");
            }
            // List<org.nc0.prjcomp.ast.Type> argumentTypes = callExpression.getArgs().stream().map(argument -> argument.accept(typeChecker)).toList();
            assert signature != null; // ?????? pr. forgot to return within if.
            org.nc0.prjcomp.ast.Type returnType = signature.returnType();
            Frame frame = frames.get(functionName);
            return makeFunCall(returnType, frame, arguments, code);
        }

        @Override
        public Result visit(ExpCons constantExpression) {
            return new Result(switch (constantExpression.getConstant()) {
                case TRUE -> new org.nc0.prjcomp.ir.expr.Byte((byte) 1);
                case FALSE -> new org.nc0.prjcomp.ir.expr.Byte((byte) 0);
            });
        }

        @Override
        public Result visit(ExpId idExpression) {
            return new Result(new ReadReg(registerLookup(idExpression.getValue())));
        }

        @Override
        public Result visit(ExpInt integerExpression) {
            return new Result(new Int(integerExpression.getValue()));
        }

        @Override
        public Result visit(ExpUn unaryExpression) {
            Result expressionResult = unaryExpression.getExp().accept(this);
            var unary = new Unary(expressionResult.getExp(), unaryExpression.getOp());
            return new Result(unary, expressionResult.getCode());
        }

        public Result visit(ExpRead readExpression) {
            //création d’un frame pour la lecture, avec un nom qui sera rajouté
            //explicitement dans le code assembleur.
            Frame frame = new Frame(Label.named("entryReadInt"), Label.named("exitReadInt"), new LinkedList<>(), new Register(Type.INT));
            org.nc0.prjcomp.ast.Type type = new TypePrim(null, TypePrim.Prim.INT);
            List<Command> code = new LinkedList<>();
            List<org.nc0.prjcomp.ir.expr.Expression> args = new LinkedList<>();
            return makeFunCall(type, frame, args, code);
        }

        private Result makeFunCall(org.nc0.prjcomp.ast.Type type, Frame frame, List<org.nc0.prjcomp.ir.expr.Expression> args, List<Command> code) {
            assert currentFrame != null;
            Register reg = new Register(Translate.ofType(type));
            currentFrame.addLocal(reg);
            code.add(new FunCall(reg, frame, args));
            return new Result(new ReadReg(reg), code);
        }

        //Classe interne : visiteur pour la constructions des Frames
        private class FramesBuilder extends BaseVisitor<Void> {
            FramesBuilder() {
                super(null);
            }

            @Override
            public Void visit(MethodDecl function) {
                List<Register> parameters = new LinkedList<>();
                Block b = function.getBlock();
                //on ne visite pas le bloc, alors le rajouter ici dans la Map
                varToReg.put(b, new HashMap<>());
                for (Formal argument : function.getFormal()) {
                    org.nc0.prjcomp.ast.Type argType = argument.getType();
                    String argName = argument.getId().getName();
                    Register register = new Register(ofType(argType));
                    Map<String, Register> map = varToReg.get(b);
                    //System.out.println("put "+argName+"->"+register+" dans "+b);
                    map.put(argName, register);
                    parameters.add(register);
                }
                Frame frame;
                org.nc0.prjcomp.ast.Type type = function.getType();
                Type irType = ofType(type);
                frame = new Frame(Label.fresh(), Label.fresh(), parameters, new Register(irType));

                frames.put(function.getId().getName(), frame);
                if (function.getId().getName().equals("main")) {
                    mainLabel = frame.getEntryPoint();
                }
                Maps.print(varToReg);
                return null;
            }

            @Override
            public Void visit(Program program) {
                for (MethodDecl function : program.getListMethodDecl()) {
                    function.accept(this);
                }
                return null;
            }
        }
    }
}
