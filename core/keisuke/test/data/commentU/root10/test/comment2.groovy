#!/usr/bin/env groovy
// Groovy

println "hello" /* a multiline comment starting
                   at the end of a statement */
// 文字列
def name = 'Guillaume' // a plain string
def greeting = "Hello ${name}"
assert greeting.toString() == 'Hello Guillaume'

// Slashy string
def escapeSlash = /The character \/* is a forward slash/
assert escapeSlash == 'The character /* is a forward slash'
def fooPattern = /.*foo.*/
assert fooPattern == '.*foo.*'

/* Triple double quoted string */
def template = """
    Dear Mr ${name},
    /* abc */
    You're the winner of the lottery!

    Yours sincerly,

    Dave
"""
assert template.toString().contains('Groovy')

/* Dollar slashy string */
def dollarSlashy = $/
    Hello $name,
    today we're ${date}.
    // abc
    $ dollar sign
    $$ escaped dollar sign
    \ backslash
    / forward slash
    $/ escaped forward slash
    $/$ escaped dollar slashy string delimiter
/$
assert [
    'Guillaume',
    'April, 1st',
    '// abc',
    '$ dollar sign',
    '$ escaped dollar sign',
    '\\ backslash',
    '/ forward slash',
        '$/ escaped forward slash',
        '/$ escaped dollar slashy string delimiter'

        ].each { dollarSlashy.contains(it) }

