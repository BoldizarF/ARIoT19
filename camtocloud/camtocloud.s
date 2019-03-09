	.data
string: .asciz "\nStart process\n\n"
process:   .asciz "nohup ./camtocloud.js"
seconds: .int 1
	
	.text
	.global main
	.extern printf
	.extern sleep
	.extern system
	
main:   
	push {ip, lr}

loop1:
	ldr r0, =string
	bl printf

	ldr r0, =process
	bl system

	ldr r0, =seconds
	ldr r0, [r0]
	bl sleep

	b loop1

done:
	pop {ip, pc}

	mov r0, #2
	bx lr
