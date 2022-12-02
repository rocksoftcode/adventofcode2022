def input = new File('input.txt').text.split(/\n/)*.split(/\s/)
def win = [A: 'B', B: 'C', C: 'A']
def lose = [A: 'C', B: 'A', C: 'B']
def draw = [A : 'A', B: 'B', C: 'C']
def outcome = [X: lose, Y: draw, Z: win]
def points = [X: 0, Y: 3, Z: 6]
def value = [A: 1, B: 2, C: 3]
println input.sum {points[it[1]] + value[outcome[it[1]][it[0]]] }