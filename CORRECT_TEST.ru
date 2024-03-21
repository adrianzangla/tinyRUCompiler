/? CORRECTO
/? Tokens representativos

    struct impl start if else while fn ret st priv new self true false void Int Str Char Bool nil Array
    /? Los comentarios admiten cualquier símbolo ॐ 月
    /? Si el primer caracter de un literal de Int es 0 se tokenizará sólo el primer 0
    /? Resultado esperado 0 | 0 | 1020
        001020
    /? Los identificadores solo pueden contener letras mayusculas, minusculas, numeros y _.
    /? Se debe diferenciar identificadores de palabras reservadas

    /? Los identificadores de Struct deben empezar con mayusculas y terminar con mayusculas o minusculas
    /? Resultado esperado STRUCT_ID | STRUCT | STRUCT_ID | STRUCT_ID | STRUCT_ID | STRUCT_ID | MEMBER_ID | MEMBER_ID
        Struct struct AaA A_A A_a Aaa aAa aAA

    /? Los identificadores de objetos deben empezar con minusculas pueden terminar con cualquier caracter valido para IDs,
    /? Resultado esperado STRUCT_ID | MEMBER_ID | MEMBER_ID | MEMBER_ID | MEMBER_ID
        Object object aaA aaa a_a aa_

    /? Los literales de Str solo pueden tener algunos simbolos y caracteres de escape definidos
    /? El escape de ' en un literal de Str es opcional
        "\b\f\n\r\t\v\"\\!@#$%^&*()_+-=[]{}ñ¿¡,.;:<>/|"
        "'" "\'" "\''"

    /? Los literales de Char solo pueden tener algunos simbolos y caracteres de escape definidos
    /? El escape de " en un literal de Char es opcional
       'ñ'
       '"' '\"'

    /? Otros simbolos
    !%&&*()--->-===!=>=<=<>[]{};:,./

    /? /?/???/?/?/?/???????//??/??????!@#$%^&*()_+}{';/.,,<>?:"{}|``~~~~"'aaaaaaa000000!!!!!!11111}///\\\\\??????
    /?//ADSFKAAFPOJOWQEFAS/FDLQ,E;LW,F.MEMBER_ID
    /?   /?
    /?
    /?
