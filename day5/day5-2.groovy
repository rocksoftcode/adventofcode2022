def parts = new File('input.txt').text.split(/\n\n/)
def stacks = parts[0].split(/\n/).last().trim().split(/\s+/).collect {new ArrayList<String>()}
parts[0].split(/\n/)[0..-2].each {
	it.findAll(/(...)\s?+/).eachWithIndex {c, i -> if (c.trim()) stacks[i] << c[1]}
}
parts[1].split(/\n/).each {
	def moves = it.split(/\s/)
	def amount = moves[1].toInteger()
	def from = moves[3].toInteger() - 1
	def to = moves[5].toInteger() - 1
	def crates = []
	amount.times {crates << stacks[from].pop()}
	crates.reverse().each {stacks[to].push(it)}
}
println stacks.collect {it[0]}.join('')
