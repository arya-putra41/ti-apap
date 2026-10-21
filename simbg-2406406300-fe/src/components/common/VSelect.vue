<script setup lang="ts">
withDefaults(defineProps<{
    id: string;
    label?: string | null;
    name?: string;
    required?: boolean;
    disabled?: boolean;
    modelValue: string;
}>(), {
    modelValue: '',
    required: false,
    disabled: false,
});

const emit = defineEmits(['update:modelValue'])
const handleInput = (e: Event) => {
    const target = e.target as HTMLInputElement;
    if (target) {
        emit('update:modelValue', target.value)
    }
}
</script>

<template>
    <div class="flex flex-col gap-1 w-full">
        <label :for="id" v-if="label">{{ label }} <span v-if="required" class="text-red">*</span></label>
        <select :id="id" :name="name" :value="modelValue" :disabled="disabled" class="rounded-lg disabled:bg-gray-200 disabled:cursor-not-allowed" @input="handleInput">
            <slot></slot>
        </select>
    </div>
</template>