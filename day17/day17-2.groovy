def chunk = {l, size ->
	def res = []
	for (def i = 0; i < l.size(); i += size) res << l[i..Math.min(i + size - 1, l.size()-1)]
	return res
}
def shapes = [
		[[1, 1, 1, 1]],
		[[0, 1, 0], [1, 1, 1], [0, 1, 0]],
		[[1, 1, 1], [0, 0, 1], [0, 0, 1]], // this one is rotated to fit my interpretation
		[[1], [1], [1], [1]],
		[[1, 1], [1, 1]]]

def dirs = ['>': 1, '<': -1]
def vents = new File('input.txt').text.split('').toList()
def vent = 0
def height = 0
def shapeNr = 0
def heights = []
def screen = []
def pow2 = (0..10).collect {Math.pow(2, it) as Integer}

def advanceHeight = {
	while (screen[height] != 0) height++
}

def getScreen = {x, y -> return ((screen[y] ?: 0) & pow2[x]) >> x}
def setScreen = {x, y -> screen[y] += pow2[x]}

def advance = {rock ->
	while (true) {
		def dir = dirs[vents[vent % vents.size()]]
		def y = -1
		if (rock.shape.every {row ->
			{
				y++
				def x = -1
				return row.every {v ->
					x++
					if (v == 0) return true
					if (dir == -1 && rock.x + dir + x < 0) return false
					if (dir == 1 && rock.x + dir + x > 6) return false
					return getScreen(rock.x + x + dir, rock.y + y) == 0
				}
			}
		}) rock.x += dir
		vent++

		y = -1
		if (rock.shape.every {row ->
			y++
			def x = -1
			row.every {v ->
				{
					x++
					if (v == 0) return true
					if (rock.y + y < 1) return false
					return getScreen(rock.x + x, rock.y - 1 + y) == 0
				}
			}
		}) rock.y-- else break
	}

	rock.shape.eachWithIndex {row, y ->
		row.eachWithIndex {v, x ->
			if (v == 0) return true
			setScreen(x + rock.x, y + rock.y)
		}
	}

	advanceHeight()
}

def create = {
	while (screen.size() <= height + 5) screen << 0
	return [
			shape: shapes[(shapeNr++) % 5],
			x    : 2,
			y    : height + 3
	]
}

def reset = {vent = 0; height = 0; shapeNr = 0; heights = []; screen = []}
def tick = {steps ->
	def lastHeight = height
	steps.times {
		advance(create())
	}
	heights << height - lastHeight
}

def findSequence = {
	def checkChunksSum = {chunkSize ->
		def tmp = chunk(heights, chunkSize).collect {c -> c.inject(0) {a, v -> a + v}}
		def i = 0
		return (tmp.size() > 5 && tmp.every {v ->
			if (i++ == tmp.size() - 1) return true
			return v == tmp[0]
		})
	}

	reset()
	tick(vents.size() * shapes.size())
	def i = 20000, i2 = i / 4
	i.times {tick(1)}
	heights.pop()
	for (def n = 2; n < i2; n++) if (checkChunksSum(n)) return n
}

def times = 1_000_000_000_000
def fst = vents.size() * shapes.size()
def step = findSequence()
reset()
tick(fst)
def offset = height
tick(step)
def mult = height - offset
tick(((times - fst - step) % step))
def part2 = (height + mult * BigDecimal.valueOf(Math.floor(-1 + (times - fst) / step)))
println part2
