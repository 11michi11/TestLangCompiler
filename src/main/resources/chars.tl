[
    letters: col<letter> = <'a', 'b', 'b', 'd', 'e'>;
    otherLetters: col<letter> = <'f', 'g', h'>;

    || array concatenation

    letters = letters + otherLetters;
    notPrinted: col<letter>{10}; || array of type letter with fixed size of 5

    size: number = letters';
    shouldPrint: state = false;

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