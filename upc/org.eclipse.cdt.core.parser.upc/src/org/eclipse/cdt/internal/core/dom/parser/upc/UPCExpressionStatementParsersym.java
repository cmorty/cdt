/*******************************************************************************
* Copyright (c) 2006, 2008 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     IBM Corporation - initial API and implementation
*********************************************************************************/

// This file was generated by LPG

package org.eclipse.cdt.internal.core.dom.parser.upc;

public interface UPCExpressionStatementParsersym {
    public final static int
      TK_auto = 33,
      TK_break = 90,
      TK_case = 91,
      TK_char = 38,
      TK_const = 6,
      TK_continue = 92,
      TK_default = 93,
      TK_do = 94,
      TK_double = 39,
      TK_else = 95,
      TK_enum = 51,
      TK_extern = 34,
      TK_float = 40,
      TK_for = 96,
      TK_goto = 97,
      TK_if = 98,
      TK_inline = 35,
      TK_int = 41,
      TK_long = 42,
      TK_register = 36,
      TK_restrict = 7,
      TK_return = 99,
      TK_short = 43,
      TK_signed = 44,
      TK_sizeof = 17,
      TK_static = 30,
      TK_struct = 52,
      TK_switch = 100,
      TK_typedef = 37,
      TK_union = 53,
      TK_unsigned = 45,
      TK_void = 46,
      TK_volatile = 8,
      TK_while = 101,
      TK__Bool = 47,
      TK__Complex = 48,
      TK__Imaginary = 49,
      TK_integer = 18,
      TK_floating = 19,
      TK_charconst = 20,
      TK_stringlit = 21,
      TK_identifier = 1,
      TK_Completion = 3,
      TK_EndOfCompletion = 5,
      TK_Invalid = 102,
      TK_LeftBracket = 31,
      TK_LeftParen = 2,
      TK_LeftBrace = 32,
      TK_Dot = 61,
      TK_Arrow = 76,
      TK_PlusPlus = 15,
      TK_MinusMinus = 16,
      TK_And = 14,
      TK_Star = 4,
      TK_Plus = 9,
      TK_Minus = 10,
      TK_Tilde = 22,
      TK_Bang = 23,
      TK_Slash = 62,
      TK_Percent = 63,
      TK_RightShift = 57,
      TK_LeftShift = 58,
      TK_LT = 64,
      TK_GT = 65,
      TK_LE = 66,
      TK_GE = 67,
      TK_EQ = 71,
      TK_NE = 72,
      TK_Caret = 73,
      TK_Or = 74,
      TK_AndAnd = 75,
      TK_OrOr = 77,
      TK_Question = 78,
      TK_Colon = 68,
      TK_DotDotDot = 60,
      TK_Assign = 69,
      TK_StarAssign = 79,
      TK_SlashAssign = 80,
      TK_PercentAssign = 81,
      TK_PlusAssign = 82,
      TK_MinusAssign = 83,
      TK_RightShiftAssign = 84,
      TK_LeftShiftAssign = 85,
      TK_AndAssign = 86,
      TK_CaretAssign = 87,
      TK_OrAssign = 88,
      TK_Comma = 54,
      TK_RightBracket = 59,
      TK_RightParen = 50,
      TK_RightBrace = 55,
      TK_SemiColon = 70,
      TK_MYTHREAD = 24,
      TK_THREADS = 25,
      TK_UPC_MAX_BLOCKSIZE = 26,
      TK_relaxed = 11,
      TK_shared = 12,
      TK_strict = 13,
      TK_upc_barrier = 103,
      TK_upc_localsizeof = 27,
      TK_upc_blocksizeof = 28,
      TK_upc_elemsizeof = 29,
      TK_upc_notify = 104,
      TK_upc_fence = 105,
      TK_upc_wait = 106,
      TK_upc_forall = 107,
      TK_ERROR_TOKEN = 56,
      TK_EOF_TOKEN = 89;

      public final static String orderedTerminalSymbols[] = {
                 "",
                 "identifier",
                 "LeftParen",
                 "Completion",
                 "Star",
                 "EndOfCompletion",
                 "const",
                 "restrict",
                 "volatile",
                 "Plus",
                 "Minus",
                 "relaxed",
                 "shared",
                 "strict",
                 "And",
                 "PlusPlus",
                 "MinusMinus",
                 "sizeof",
                 "integer",
                 "floating",
                 "charconst",
                 "stringlit",
                 "Tilde",
                 "Bang",
                 "MYTHREAD",
                 "THREADS",
                 "UPC_MAX_BLOCKSIZE",
                 "upc_localsizeof",
                 "upc_blocksizeof",
                 "upc_elemsizeof",
                 "static",
                 "LeftBracket",
                 "LeftBrace",
                 "auto",
                 "extern",
                 "inline",
                 "register",
                 "typedef",
                 "char",
                 "double",
                 "float",
                 "int",
                 "long",
                 "short",
                 "signed",
                 "unsigned",
                 "void",
                 "_Bool",
                 "_Complex",
                 "_Imaginary",
                 "RightParen",
                 "enum",
                 "struct",
                 "union",
                 "Comma",
                 "RightBrace",
                 "ERROR_TOKEN",
                 "RightShift",
                 "LeftShift",
                 "RightBracket",
                 "DotDotDot",
                 "Dot",
                 "Slash",
                 "Percent",
                 "LT",
                 "GT",
                 "LE",
                 "GE",
                 "Colon",
                 "Assign",
                 "SemiColon",
                 "EQ",
                 "NE",
                 "Caret",
                 "Or",
                 "AndAnd",
                 "Arrow",
                 "OrOr",
                 "Question",
                 "StarAssign",
                 "SlashAssign",
                 "PercentAssign",
                 "PlusAssign",
                 "MinusAssign",
                 "RightShiftAssign",
                 "LeftShiftAssign",
                 "AndAssign",
                 "CaretAssign",
                 "OrAssign",
                 "EOF_TOKEN",
                 "break",
                 "case",
                 "continue",
                 "default",
                 "do",
                 "else",
                 "for",
                 "goto",
                 "if",
                 "return",
                 "switch",
                 "while",
                 "Invalid",
                 "upc_barrier",
                 "upc_notify",
                 "upc_fence",
                 "upc_wait",
                 "upc_forall"
             };

    public final static boolean isValidForParser = true;
}
