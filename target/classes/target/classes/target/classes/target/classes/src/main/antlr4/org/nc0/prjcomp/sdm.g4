grammar sdm; // software, data and methods

@header {
    package org.nc0.prjcomp.parser;
}

// Lexèmes

Id : [a-zA-Z][0-9a-zA-Z_]*;
Int : [0-9]+;

WS : [ \t\r\n]+ -> skip;
Char : '\'' . '\'';

// Grammaire

program : (methodDecl)*mainMethod;

mainMethod : 'org.nc0.prjcomp.main' '(' ')' statement;
methodDecl : type Id '(' (formal (',' formal)*)? ')' statement ;

formal : type Id;

type : 'int'		#intType
	|'boolean'	#boolType
;
statement : '{' statement* '}'					#statList
	|'if' '(' exp ')' statement 'else' statement		#statIf
	|'while' '(' exp ')' statement				#statWhile
	|'for' '(' statement  exp ';'  statement ')' statement  #statFor
	|'print' '(' exp ')' ';'				#statPrint
	|Id '=' exp ';'						#statAff
	|Id '++' ';'						#statIncr
	|'return' exp ';'					#statReturn
	|type Id ';'						#statVarDecl
	|type Id '=' exp ';'					#statVarDeclAff
;

exp : exp op=('&&' |'<' |'>'|'!='|'/'| '+' | '-' |'*'|'=='|'+='|'||'|'<='|'>=') exp	#exBinop
	|op=('!'|'-') exp								#exUnop
	|Int										#exInt	
	|'true'										#exTrue
	|'false'									#exFalse	
	|Id										#exId
	|'(' exp ')'									#exParenthesis
	|Id '(' (exp (',' exp)*)?  ')'							#exCall
	|'read' '(' ')'									#exRead
;


