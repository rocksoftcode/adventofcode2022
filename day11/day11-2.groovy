class Monkey {
	int index
	List<BigInteger> items
	String operation
	int divisible
	Map<Boolean, Integer> outcomes
	BigInteger inspections = new BigInteger("0")
}

def mod = 1 as BigInteger
def monkeys = []
def input = new File('input.txt').text.split(/\n\n/)
input.eachWithIndex {it, i ->
	def parts = it.split(/\n/)
	def items = parts.find {it.contains('Starting items')}.split(':')*.trim()[1].split(',').collect {it.trim().toBigInteger()}.toList()
	def operation = parts.find {it.contains('Operation')}.split(':')[1].trim().split('new =')[1]
	def divisible = parts.find {it.contains('Test')}.split(':')[1].split(/\s/).last().toBigInteger()
	def outcomes = [(Boolean.TRUE): parts.find {it.contains('If true')}.split(':')[1].split(/\s/).last().toBigInteger()]
	outcomes[Boolean.FALSE] = parts.find {it.contains('If false')}.split(':')[1].split(/\s/).last().toBigInteger()
	mod *= divisible as BigInteger
	monkeys << new Monkey(index: i, items: items, operation: operation, divisible: divisible, outcomes: outcomes)
}
def calc = { it[1] == '*' ? it[0].toBigInteger() * it[2].toBigInteger() : it[0].toBigInteger() + it[2].toBigInteger() }
10_000.times {
	monkeys.each {m ->
		m.inspections+=m.items.size()
		while (m.items) {
			def i = m.items.pop()
			def result = calc(m.operation.replaceAll('old', i.toString()).split(/\s/).findAll {it})
			result %= mod
			monkeys[m.outcomes[(result % m.divisible == 0)]].items << result
		}
	}
}

def byInspection = monkeys.sort { it.inspections }.reverse()
println byInspection[0].inspections * byInspection[1].inspections