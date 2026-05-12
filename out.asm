	.data
buffer:
	.asciiz "  "
	.text
main:
	jal L2
	li $v0, 10
	syscall
