def values = [:]
('a'..'z').eachWithIndex { it, i -> values[it] = i+1 }
('A'..'Z').eachWithIndex { it, i -> values[it] = i+27 }
def packs = new File('input.txt').text.split(/\n/)*.split('')*.toList()
def sum = 0
for (def i= 0; i < packs.size(); i += 3) {
	def pack1 = packs[i]
	def pack2 = packs[i+1]
	def pack3 = packs[i+2]
	sum += values[pack3.find { pack1.contains(it) && pack2.contains(it) }]
}
println sum