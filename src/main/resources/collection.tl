[
    var numbers: col<number>;
    numbers = <3, 1, 5, 4, 2>;
	var nums: col<number> {5};
    var n: number;
    n = numbers';
    var i: number;
    i = 1;

    until (i < n) [
        var j: number = 1;
        until (j < n-i) [
            ? (numbers<j> > numbers<j+1>) [
                var temp: number;
                temp =  = numbers<j>;
                numbers<j> = numbers<j+1>;
                numbers<j+1> = temp;
            ]
            j = j + 1;
        ]
        i = i+1;
    ]
]