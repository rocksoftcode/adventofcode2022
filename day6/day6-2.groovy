def input = new File('input.txt').text.split('').toList()
def i = 0
while (i++ < input.size() - 14) if (input[i..i+13].unique().size() == 14) break
println i + 14