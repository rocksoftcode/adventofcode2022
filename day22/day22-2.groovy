def map = []
def cols = 0
def size
def input = new File('input.txt').text.split(/\n\n/)
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
def createSubMap = {
	def div = rows > cols ? [3, 4] : [4, 3]
	size = cols/div[0]
	side = 1
	def subMap = Arrays.asList(new Integer[div[1]][div[0]])
	for (def row = 0; row < div[1]; row++)
		for (def col = 0; col < div[0]; col++)
			if (map[row * size][col * size] != null) subMap[row][col] = side++
	subMap
}
def sideMap = createSubMap()
def sideVal = { sideMap[Math.floor(it.row / size).intValue()][Math.floor(it.col / size).intValue()] }
def val = { (it.row+1)*1000+(it.col+1)*4+dirs[it.dir] }
def mapAt = { map[it.row][it.col] }
def next = { p ->
	def i = inc[p.dir]
	def tPos = p.collectEntries { it }
	def from = sideVal(p)

	tPos.row += i[1]
	tPos.col += i[0]

	def to = (tPos.row < 0 || tPos.col < 0 || tPos.row >= rows || tPos.col >= cols) ? null : sideVal(tPos)
	if (to == from) return tPos
	if (to) return tPos

	if (from == 1 && tPos.dir == '^') {
		tPos.dir = '>'
		tPos.row = 150+p.col-50
		tPos.col = 0
	} else if (from == 1 && tPos.dir == '<') {
		tPos.dir = '>'
		tPos.row = 100+49-p.row
		tPos.col = 0
	}
	if (from == 2 && tPos.dir == '^') {
		tPos.col = p.col-100
		tPos.row = 199
	} else if (from == 2 && tPos.dir == '>') {
		tPos.dir = '<'
		tPos.col = 99
		tPos.row = 100+49-p.row
	} else if (from == 2 && tPos.dir == 'v') {
		tPos.dir = '<'
		tPos.col = 99
		tPos.row = p.col-100+50
	}
	if (from == 3 && tPos.dir == '>') {
		tPos.dir = '^'
		tPos.col = p.row-50+100
		tPos.row = 49
	} else if (from == 3 && tPos.dir == '<') {
		tPos.dir = 'v'
		tPos.col = p.row-50+0
		tPos.row = 100
	}
	if (from == 4 && tPos.dir == '<') {
		tPos.dir = '>'
		tPos.col = 50
		tPos.row = 149-p.row
	} else if (from == 4 && tPos.dir == '^') {
		tPos.dir = '>'
		tPos.col = 50
		tPos.row = p.col+50
	}
	if (from == 5 && tPos.dir == '>') {
		tPos.dir = '<'
		tPos.col = 149
		tPos.row = 149-p.row
	} else if (from == 5 && tPos.dir == 'v') {
		tPos.dir = '<'
		tPos.col = 49
		tPos.row = p.col-50+150
	}
	if (from == 6 && tPos.dir == 'v') {
		tPos.col = p.col+100
		tPos.row = 0
	} else if (from == 6 && tPos.dir == '>') {
		tPos.dir = '^'
		tPos.col = p.row-150+50
		tPos.row = 149
	} else if (from == 6 && tPos.dir == '<') {
		tPos.dir = 'v'
		tPos.col = p.row-150+50
		tPos.row = 0
	}
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