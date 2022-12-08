def input = new File('input.txt').text.split(/\n/).toList()*.split('')*.collect {it.toInteger()}
def highScore = 0
def up = {x, y -> (0..y - 1).reverse().collect {input[it][x]}}
def down = {x, y -> (y + 1..input.size() - 1).collect {input[it][x]}}
def left = {x, y -> (0..x - 1).reverse().collect {input[y][it]}}
def right = {x, y -> (x + 1..input[0].size() - 1).collect {input[y][it]}}
def view = {v, t ->
	def r = t.findIndexOf {it >= v}
	r == -1 ? t.size() : r + 1
}
(1..input.size() - 2).each {y ->
	(1..input[0].size() - 2).each {x ->
		def v = input[y][x]
		highScore = Integer.max(highScore, view(v, up(x, y)) * view(v, down(x, y)) * view(v, left(x, y)) * view(v, right(x, y)))
	}
}
println highScore