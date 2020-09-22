[
    numbers: col<number> = <3, 1, 5, 4, 2>;

    n: number = numbers';
    i: number = 1;

    until (i < n) [
        j: number = 1;
        until (j < n-i) [
            ? (numbers<j> > numbers<j+1>) [
                temp: number = numbers<j>;
                numbers<j> = numbers<j+1>;
                numbers<j+1> = temp;
            ]
            j = j + 1;
        ]
        i = i+1;
    ]
]