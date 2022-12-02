def score = [A: 1, B: 2, C: 3]
def lookup = [X: 'A', Y: 'B', Z: 'C']
def win = [A: 'C', B: 'A', C: 'B']
def input = new File('input.txt').text.split(/\n/)*.split(/\s/)
println input.sum { (lookup[it[1]] == it[0] ? 3 : (it[0] == win[lookup[it[1]]] ? 6 : 0)) + score[lookup[it[1]]] }