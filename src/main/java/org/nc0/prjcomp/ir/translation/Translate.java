// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ir.translation;

import org.nc0.prjcomp.ast.*;
import org.nc0.prjcomp.ir.Frame;
import org.nc0.prjcomp.ir.Register;
import org.nc0.prjcomp.ir.Type;
import org.nc0.prjcomp.ir.com.Command;
import org.nc0.prjcomp.ir.com.FunCall;
import org.nc0.prjcomp.ir.com.Label;
import org.nc0.prjcomp.ir.expr.ReadReg;
import org.nc0.prjcomp.semantic.MethodSig;
import org.nc0.prjcomp.semantic.SymbolTable;
import org.nc0.prjcomp.semantic.TypeChecker;
import org.nc0.prjcomp.support.Errors;
import org.nc0.prjcomp.support.ListTools;
import org.nc0.prjcomp.support.Maps;
import org.nc0.prjcomp.support.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Translate {
    private static final TypeConverter typeConverter = new TypeConverter();

    private static org.nc0.prjcomp.ir.Type ofType(org.nc0.prjcomp.ast.Type type) {
        return type.accept(typeConverter);
    }

    public static Pair<Label, List<Pair<Frame, List<Command>>>> run(SymbolTable symbolTable, Program program) {
        TranslationVisitor translator = new TranslationVisitor(symbolTable);
        program.accept(translator);
        Errors e = translator.errors;
        if (translator.mainLabel == null) {
            e.add("pas de fonction org.nc0.prjcomp.main.");
        }
        if (e.hasErrors()) {
            System.out.println("Erreurs de traduction en code intermédiaire :");
            e.print();
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
        public org.nc0.prjcomp.ir.Type visit(TypePrim type) {
            return switch (type.getPrim()) {
                case INT -> Type.INT;
                case BOOL -> Type.BYTE;
                default -> throw new Error("Traduction IR :type primitif non géré : " + type.getPrim().toString());
            };
        }
    }

    private static class TranslationVisitor extends org.nc0.prjcomp.ast.BaseVisitor<Result> {
        //La table des symboles et le typeChecker, utiles pour au moins deux
        //éléments :
        //- Savoir à quel bloc se réfère un nom de variable (pile des blocs)
        //- Savoir quel est le type pour les registres associés aux variables.
        private final SymbolTable symbolTable;
        private final TypeChecker typeChecker;
        //Chaque nom de méthode est associé à un frame (construit par le
        //FramesBuilder)
        private final Map<String, Frame> frames;
        //Dans chaque bloc, chaque variable est associée à un registre.
        private final Map<Block, Map<String, Register>> varToReg;
        //Chaque méthode sera enregistrée dans une liste de paires, avec son
        //frame et son code.
        private final List<Pair<Frame, List<Command>>> fragments;
        //Le frame courant, auquel on ajoute en particulier les registres
        //temporaires nécessaires au calcul de la fonction (qui seront empilés
        //lors de l’appel).
        private final Frame currentFrame;
        public Errors errors;
        //Un label spécifique, correspondant au point d’entrée de la fonction
        //org.nc0.prjcomp.main.
        private Label mainLabel;

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

        //----REGISTRES-------
        private Register registerLookup(String name) {
            Register reg;
            for (Block block : typeChecker.getVisitedBlocks().getStack()) {
                Map<String, Register> map = varToReg.get(block);

                reg = map.get(name);
                //System.out.println("lookup "+block+": "+name);
                if (reg != null)
                    return reg;
            }
            Maps.print(varToReg);
            errors.add("Erreur interne, pas de registre associé à la variable " + name);
            return null;
        }

        @Override
        public Result visit(MethodDecl md) {
            //TODO
            //-Récupérer le frame associé à la méthode,
            //-Récupérer le code du corps de la fonction
            //-Ajouter la paire frame/code aux fragments
            return null;
        }

        @Override
        public Result visit(Program program) {
            program.accept(typeChecker);
            FramesBuilder framesBuilder = new FramesBuilder();
            program.accept(framesBuilder);
            for (MethodDecl fun : program.getListMethodDecl())
                fun.accept(this);
            return null;
        }

        @Override
        public Result visit(StatAff s) {
            //	- Compilation de l’expression
            //	- Récupération du registre lié à la variable (le registre
            //	devrait être dans la Map, si les phases précédentes ont bien
            //	fait leur travail.)
            //	- Renvoyer le code permettant de calculer l’expression, auquel
            //	on ajoute une écriture de l’expression dans le registre
            //	obtenu au point précédent.
            return null;
        }

        @Override
        public Result visit(StatIf stm) {
            //TODO
            return null;
        }

        @Override
        public Result visit(StatList sl) {
            List<Command> code = new LinkedList<>();
            for (Statement stm : sl.getStatList()) {
                Result result = stm.accept(this);
                code.addAll(result.getCode());
            }
            return new Result(code);
        }

        @Override
        public Result visit(StatPrint s) {
            Expression e = s.getExpression();
            Frame frame = new Frame(Label.named("entryPrintInt"), Label.named("exitPrintInt"), ListTools.mklist(new Register(org.nc0.prjcomp.ir.Type.INT)), new Register(org.nc0.prjcomp.ir.Type.INT));
            Result r = e.accept(this);

            List<Command> code = r.getCode();
            List<org.nc0.prjcomp.ir.expr.Expression> args = ListTools.mklist(r.getExp());
            //on laisse un int en retour par défaut, car toutes les fonctions
            //doivent retourne qqchose.
            org.nc0.prjcomp.ast.Type type = new TypePrim(null, TypePrim.Prim.INT);

            return makeFunCall(type, frame, args, code);
        }

        private Result makeFunCall(org.nc0.prjcomp.ast.Type type, Frame frame, List<org.nc0.prjcomp.ir.expr.Expression> args, List<Command> code) {
            Register reg = new Register(ofType(type));
            assert currentFrame != null;
            currentFrame.addLocal(reg);
            Command call = new FunCall(reg, frame, args);
            code.add(call);
            return new Result(new ReadReg(reg), code);
        }


        //-------INSTRUCTIONS--------

        @Override
        public Result visit(StatWhile stm) {
            //TODO
            return null;
        }

        @Override
        public Result visit(StatVarDecl stm) {
            // Declaration de variable : nouveau registre
            // TODO :
            // 	-Créer un registre (avec le bon type)
            // 	-Récupérer le bloc actuel (celui déterminé par le typeChecker
            //	-Mettre l’id et le registre dans la map 'varToReg'
            //	-Ajouter le registre au frame courant.
            //	-Retourner un code vide.
            return null;
        }

        @Override
        public Result visit(StatReturn stm) {
            //compiler l’expression, récupérer le registre de retour du
            //frame courant, puis produire le code qui :
            //  - permet de calculer l’expression
            //  - écrit cette expression dans le registre de retour
            //  - saute au point de sortie du frame courant
            //  TODO
            return null;
        }

        @Override
        public Result visit(Block b) {
            varToReg.computeIfAbsent(b, k -> new HashMap<>());
            typeChecker.getVisitedBlocks().enter(b);

            Statement stm = b.getStatement();
            Result result = stm.accept(this);
            List<Command> code = new LinkedList<>(result.getCode());
            typeChecker.getVisitedBlocks().exit();
            return new Result(code);
        }

        @Override
        public Result visit(ExpBin exp) {
            //TODO
            return null;
        }

        @Override
        public Result visit(ExpCallMethod exp) {
            String functionName = exp.getMethod().getName();

            Pair<List<org.nc0.prjcomp.ir.expr.Expression>, List<Command>> translation = translateExpressions(exp.getArgs());
            assert translation != null;
            List<Command> code = translation.snd();
            List<org.nc0.prjcomp.ir.expr.Expression> arguments = translation.fst();
            MethodSig signature = symbolTable.methodLookup(functionName);
            if (signature == null)
                errors.add("Erreur interne, " + functionName + " pas dans la table");

            List<org.nc0.prjcomp.ast.Type> argumentTypes = getArgumentTypes(exp.getArgs());
            assert signature != null;
            org.nc0.prjcomp.ast.Type returnType = signature.returnType();

            Frame frame = frames.get(functionName);
            return makeFunCall(returnType, frame, arguments, code);
        }

        //Pour compiler une liste d’expressions dans une seule paire (exp,code)
        private Pair<List<org.nc0.prjcomp.ir.expr.Expression>, List<Command>> translateExpressions(List<Expression> exps) {
            //TODO
            return null;
        }

        //Pour récupérer la liste des types de la liste des paramètres
        List<org.nc0.prjcomp.ast.Type> getArgumentTypes(List<Expression> args) {
            return args.stream().map((a) -> a.accept(typeChecker)).collect(Collectors.toList());
        }

        //---EXPRESSIONS------
        @Override
        public Result visit(ExpCons exp) {
            //TODO
            //Retourner un octet = à 1 pour true, 0 pour false.
            return null;
        }

        @Override
        public Result visit(ExpId exp) {
            //TODO Retourner une seule instruction, celle qui lit dans le
            //registre associé à la variable.
            return null;
        }

        @Override
        public Result visit(ExpInt exp) {
            //TODO
            return null;
        }

        @Override
        public Result visit(ExpUn exp) {
            //TODO
            return null;
        }

        public Result visit(ExpRead e) {
            //création d’un frame pour la lecture, avec un nom qui sera rajouté
            //explicitement dans le code assembleur.
            Frame frame = new Frame(Label.named("entryReadInt"), Label.named("exitReadInt"), new LinkedList<>(), new Register(org.nc0.prjcomp.ir.Type.INT));
            org.nc0.prjcomp.ast.Type type = new TypePrim(null, TypePrim.Prim.INT);
            List<Command> code = new LinkedList<>();
            List<org.nc0.prjcomp.ir.expr.Expression> args = new LinkedList<>();
            return makeFunCall(type, frame, args, code);
        }

        //Classe interne : visiteur pour la constructions des Frames
        private class FramesBuilder extends org.nc0.prjcomp.ast.BaseVisitor<Void> {
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
                org.nc0.prjcomp.ir.Type irType = ofType(type);
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
