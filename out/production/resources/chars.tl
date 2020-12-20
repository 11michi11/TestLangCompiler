[
    var letters: col<letter>;
    letters  = <'a', 'b', 'b', 'd', 'e'>;
    var otherLetters: col<letter>;
     otherLetters = <'f', 'g', h'>

    # array concatenation

    letters = letters + otherLetters;
    var notPrinted: col<letter>{10}; # array of type letter with fixed size of 5

    var size: number;
    size = letters';
    var shouldPrint: state;
    shouldPrint = false;

    until (size == 0) [
        ? (shouldPrint) [
            out <= letters//;
        ]
        ? (!shouldPrint) [
            notPrinted << letters//;
        ]
        size--;
        shouldPrint = !shouldPrint;
    ]
]