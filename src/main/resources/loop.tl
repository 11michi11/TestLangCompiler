[
    counter: number;
    result: number;

    in => counter;

    until (counter > 0) [
        result = result  + calculate(counter, result);
        counter--;
    ]

    out <= result;

    opr calculate(a: number, b: number): number [
        result: number = a * b + 2;
        send result;
    ]
]