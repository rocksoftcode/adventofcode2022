def input = new File('input.txt').text.split(/\n/).toList()*.split('')*.collect {it.toInteger()}
def up = {x, y -> (0..y - 1).reverse().collect {input[it][x]}}
def down = {x, y -> (y + 1..input.size() - 1).collect {input[it][x]}}
def left = {x, y -> (0..x - 1).reverse().collect {input[y][it]}}
def right = {x, y -> (x + 1..input[0].size() - 1).collect {input[y][it]}}
def count = (input.size() * 2) + ((input[0].size() - 2) * 2)
(1..input.size() - 2).each {y ->
	(1..input[0].size() - 2).each {x ->
		if ([up, down, left, right].any {it(x, y).every {it < input[y][x]}}) count++
	}
}
println count