[

    result = 5;

    var counter: number;
    var result: number;
    out <= result;
  #  in => counter;

  #  until (counter > 0) [
  #      result = result  + calculate(counter, result);
  #      counter = counter - 1;
  #  ]

  #  out <= result;

  #  number opr calculate(a: number, b: number): number [
  #      var result: number;
  #      result = a * b + 2;
  #      send result;
  #  ]
]