<?php
# コメント1
class foo
{
    var $foo;
    var $bar;

    function foo()
    {
        $this->foo = 'Foo';
        $this->bar = array('Bar1', 'Bar2', 'Bar3');
    }
}

$foo = new foo();
$name = 'MyName'; // コメント２

/* コメント3 */

echo <<<EOT
#My name is "$name". I am printing some $foo->foo.
Now, I am printing some {$foo->bar[0]}.
This should print a capital 'A': \x41
EOT;

echo <<<"EOD"
//My name is "$name". I am printing some $foo->foo.
Now, I am printing some {$foo->bar[1]}.
This should print a capital 'A': \x41
EOD ;

echo <<<'EOS'
/*My name is "$name". I am printing some $foo->foo.*/
Now, I am printing some {$foo->bar[2]}.
This should not print a capital 'A': \x41
EOS;
?>
