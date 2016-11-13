#include <stdio.h>
#include <stdlib.h>

int factorial(int num)
{
        int f, n; /* sss */
 /**/
        if (num == 0) {
                return 1;
        }
        /*
        n = num - 1;
        for (f = num; n >= 1; n--) {
                f = f * n;
        }
        */
        return f;
}

int main(int argc, const char *argv[])
{
        int n, f;

        if (argc != 2) {
                return 0;
        }

        n = atoi(argv[1]);
        f = factorial(n);
        printf("%d\n", f);

        return 0;
}
