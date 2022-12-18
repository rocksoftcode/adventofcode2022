def input = new File('input.txt').text.split(/\n/)*.split(',').collect {it*.toInteger()}.toList()
def droplets = input.collect {[it[0] + 1, it[1] + 1, it[2] + 1]}
def maxX = droplets.max {it[0]}[0] + 1
def maxY = droplets.max {it[1]}[1] + 1
def maxZ = droplets.max {it[2]}[2] + 1
def all = (0..maxX + 1).collect {(0..maxY + 1).collect {(0..maxZ + 1).collect {false}}}
droplets.each {all[it[0]][it[1]][it[2]] = true}
def sides = 0
def transforms = [[-1, 0, 0], [1, 0, 0], [0, -1, 0], [0, 1, 0], [0, 0, -1], [0, 0, 1]]
droplets.each {d -> transforms.each {t -> if (!all[d[0] + t[0]][d[1] + t[1]][d[2] + t[2]]) sides++ } }
println sides