def shapes = [
		[[1, 1, 1, 1]],
		[[0, 1, 0], [1, 1, 1], [0, 1, 0]],
		[[1, 1, 1], [0, 0, 1], [0, 0, 1]], // this one is rotated to fit my interpretation
		[[1], [1], [1], [1]],
		[[1, 1], [1, 1]]]

def dirs = ['>': 1, '<': -1]
def vents = new File('input.txt').text.split('')
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
			if (v == 0) return
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

2022.times {
	def lastHeight = height
	advance(create())
	heights << height - lastHeight
}
println height