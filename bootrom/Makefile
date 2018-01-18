.PRECIOUS: bootrom.dis
bootrom_img = bootrom.img

GCC=riscv64-unknown-elf-gcc
OBJCOPY=riscv64-unknown-elf-objcopy
OBJDUMP=riscv64-unknown-elf-objdump

all: $(bootrom_img)

%.img: %.bin %.dis
	dd if=$< of=$@ bs=128 count=1

%.dis: %.elf
	$(OBJDUMP) -d $< >$@

%.bin: %.elf
	$(OBJCOPY) -O binary $< $@

%.elf: %.S linker.ld
	$(GCC) -Tlinker.ld $< -nostdlib -static -Wl,--no-gc-sections -o $@
