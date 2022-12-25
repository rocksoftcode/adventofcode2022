def input = new File('input.txt').readLines()
def toSnafu = {decimal ->
	def result = ''
	do {
		def remainder = decimal % 5
		switch (remainder) {
			case 0:
			case 1:
			case 2:
				result = remainder + result
				break
			case 3:
				result = "=$result"
				decimal += 5
				break
			case 4:
				result = "-$result"
				decimal += 5
				break
		}
		decimal = (decimal / 5).toBigInteger()
	} while (decimal != 0)
	result
}

def toDecimal = {snafuNum ->
	def digits = snafuNum.split('').reverse()
	def decimal = 0 as BigInteger
	def mult = 1 as BigInteger
	for (def digit in digits) {
		def val = 0 as BigInteger
		if (digit == '-') {
			val = -1
		} else if (digit == '=') {
			val = -2
		} else {
			val = digit.toBigInteger()
		}
		decimal += val * mult
		mult *= 5
	}
	decimal
}

println toSnafu(input.inject(0) {a, b -> a += toDecimal(b)})