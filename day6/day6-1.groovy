def input = new File('input.txt').text.split('').toList()
def i = 0
while (i++ < input.size() - 4) if (input[i..i+3].unique().size() == 4) break
println i + 4