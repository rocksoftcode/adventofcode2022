def values = [:]
('a'..'z').eachWithIndex { it, i -> values[it] = i+1 }
('A'..'Z').eachWithIndex { it, i -> values[it] = i+27 }
def packs = new File('input.txt').text.split(/\n/)*.split('')*.toList()
println packs.sum {it ->
	def part1 = it[0..(it.size() / 2)-1]
	def part2 = it[it.size() / 2..it.size()-1]
	values[part1.find { part2.contains(it) } ]
}