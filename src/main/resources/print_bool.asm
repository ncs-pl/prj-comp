
_print_bool_ENTRY:
    bne $a0, 0, _print_bool_true
    j _print_bool_false
_print_bool_true:
	li $a0, 84
	li $v0, 11
	syscall
	j _print_bool_EXIT
_print_bool_false:
    li $a0, 70
    li $v0, 11
    syscall
_print_bool_EXIT:
	j $ra
