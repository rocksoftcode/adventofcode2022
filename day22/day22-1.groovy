def input = new File('input.txt').text.split(/\n\n/)
def map = []
def cols = 0
input[0].split(/\n/).eachWithIndex { line, row ->
	cols = Math.max(cols, line.length())
	def tmp = [null] * cols
	line.split('').eachWithIndex { v, col ->
		if (['.', '#'].contains(v)) tmp[col] = v
	}
	map << tmp
}
def rows = map.size()
def path = input[1]
def forwards = path.findAll( /\d+/)*.toInteger()
def rotations = path.findAll(/[RL]/)

def inc = ['>': [1, 0], 'v': [0, 1], '<': [-1, 0], '^': [0, -1]]
def dirs = ['>': 0, 'v': 1, '<': 2, '^': 3]
def rotates = [
	L: ['>': '^', 'v': '>', '<': 'v', '^': '<'],
	R: ['>': 'v', 'v': '<', '<': '^', '^': '>']
]

def val = { (it.row+1)*1000+(it.col+1)*4+dirs[it.dir] }
def mapAt = { map[it.row][it.col] }
def next = { p ->
	def i = inc[p.dir]
	def tPos = p.collectEntries {it }
	def step = {
		tPos.row += i[1]
		tPos.col += i[0]
		if (tPos.col >= cols) tPos.col = 0
		if (tPos.row >= rows) tPos.row = 0
		if (tPos.col < 0) tPos.col = cols-1
		if (tPos.row < 0) tPos.row = rows-1
	}
	step()
	while (!mapAt(tPos)) step()
	tPos
}
def move = { steps, nextPos ->
	while (mapAt(nextPos(pos)) == '.' && steps) {steps--; pos = nextPos(pos)}
}
pos = [dir: '>', col: map[0].indexOf('.'), row: 0]
for (def i = 0; i <= forwards.size()+rotations.size()-1; i++) {
	if (i % 2 == 0) move(forwards[i/2], next)
	if (i % 2 == 1) pos.dir = rotates[rotations[(i-1)/2]][pos.dir]
}
println val(pos)
