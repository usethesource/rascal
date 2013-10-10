module experiments::Compiler::Examples::Visit3

value main(list[value] args) {
	return visit({ [1,1], [2,2], [3,3] }) { // { [202], [204], [206], [4,5] }
				case set[value] s => s + { [4,5] }
				case list[int] l => [ ( 0 | it + i | int i <- l) ]
				case int i => i + 100
			} +
			top-down visit({ [1,1], [2,2], [3,3] }) { // { [102], [104], [106], [109] }
				case set[value] s => s + { [4,5] }
				case list[int] l => [ ( 0 | it + i | int i <- l) ]
				case int i => i + 100
			};
			// { [206], [4,5], [109], [204], [202], [106], [104], [102] }
}