/**
 * Define a grammar called IRCGrammar
 */
grammar IRCGrammar;

// Parser Rules
message : prefix? command params? crlf;
prefix : PREFIX;
command : COMMAND;
params : PARAMS;
crlf : CRLF;

// Token definitions
PARAMS: ((SPACE MIDDLE)+ (SPACE ':' TRAILING)? | (SPACE MIDDLE)+ (SPACE (':')? TRAILING)?)+;
PREFIX : ':' HOST SPACE | ':' (NICKNAME ('!' USER)? ('@' HOST)?) SPACE;
CRLF : '\n';
COMMAND : LETTER+ | DIGIT DIGIT DIGIT;
MIDDLE : NOSPCRLFCL (':' | NOSPCRLFCL)*;
NICKNAME : (LETTER | SPECIAL) (LETTER | DIGIT | SPECIAL | '-')*;
HOST : HOSTNAME | HOSTADDR;
USER : ([\u0001-\u0009] | [\u000B-\u000C] | [\u000E-\u001F] | [\u0021-\u003F] | [\u0041-\u00FF])+ | NICKNAME;

// Regular expressions used in token definitions
fragment TRAILING : (':' | NOSPCRLFCL)+ ;
fragment TARGET : NICKNAME | HOSTNAME;
fragment MSGTARGET : MSGTO (',' MSGTO)*;
fragment HOSTADDR : IP4ADDR | IP6ADDR;
fragment SPACE : '\u0020';
fragment LETTER : [a-zA-Z] ;
fragment DIGIT  : [0-9]+    ;
fragment NOSPCRLFCL : [\u0001-\u0009] | [\u000B-\u000C] | [\u000E-\u001F] | [\u0021-\u0039] |[\u003B-\u00FF];
fragment MSGTO : CHANNEL | (USER('%' HOST)? '@' HOSTNAME) | USER '&' HOST | TARGETMASK | NICKNAME | NICKNAME '!' USER '@' HOST;
fragment CHANNEL : ('#' | '+' | '!' CHANNELID | '&') CHANSTRING (':' CHANSTRING)?;
fragment HOSTNAME : SHORTNAME ('.' SHORTNAME)?;
fragment SHORTNAME : (LETTER | DIGIT) (LETTER | DIGIT| '-')*;
fragment IP4ADDR : DIGIT '.' DIGIT '.' DIGIT '.' DIGIT     ;
fragment IP6ADDR : HEXDIGIT (':' HEXDIGIT) (':' HEXDIGIT)(':' HEXDIGIT)(':' HEXDIGIT)(':' HEXDIGIT)(':' HEXDIGIT)(':' HEXDIGIT) |
'0:0:0:0:0:' ('0' | 'FFFF') ':' IP4ADDR;
fragment TARGETMASK : ('$' | '#') MASK ;
fragment CHANSTRING : [\u0001-\u0007] | [\u0008-\u0009] | [\u000B-\u000C] | [\u000E-\u001F] | [\u0021-\u002B] | [\u002D-\u0039] | [\u003B-\u00FF];
fragment CHANNELID : LETTERDIGIT LETTERDIGIT LETTERDIGIT LETTERDIGIT LETTERDIGIT;
fragment LETTERDIGIT : LETTER | DIGIT;
fragment KEY : ([\u0001-\u0005] | [\u0007-\u0008] | '\u000C' | [\u000E-\u001F] | [\u0021-\u007F])+;
fragment HEXDIGIT : DIGIT | [A-F];
fragment SPECIAL : [\u005B-\u0060] | [\u007B-\u007D];

// Wildcard expressions
fragment MASK : (NOWILD | NOESC WILDONE | NOESC WILDMANY)+ ;
fragment WILDONE : '\u003F';
fragment WILDMANY : '\u002A';
fragment NOWILD : [\u0001-\u0029] | [\u002B-\u003E] | [\u0040-\u00FF];
fragment NOESC : [\u0001-\u005B] | [\u005D-\u00FF] ;
fragment MATCHONE : [\u0001-\u00FF];
fragment MATCHMANY : MATCHONE+;



// Whitespace to be ignored
WHITESPACE : . ->skip ;