<template>
    <button class="v-button">
        <slot></slot>
    </button>
</template>

<style scoped>
    @reference "@/assets/main.css";

    .v-button {
        @apply text-lg font-semibold hover:cursor-pointer py-1 px-5 rounded-2xl
    }
</style>