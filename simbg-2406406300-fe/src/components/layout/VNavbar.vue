<script setup lang="ts">
import { RouterLink, useRoute } from 'vue-router';

const route = useRoute();

const getLinkClass = (path: string) => route.path === path ? 'text-pink-600' : 'text-black hover:text-pink-600'
</script>

<template>
    <header class="fixed z-50 flex items-center w-full gap-4 px-3 py-4 bg-white">
        <RouterLink to="/" class="text-xl font-bold text-pink-600">SIMBG</RouterLink>
        <nav class="flex gap-4">
            <RouterLink to="/sekolah" :class="getLinkClass('/sekolah')">Sekolah</RouterLink>
            <RouterLink to="/rombel" :class="getLinkClass('/rombel')">Rombel</RouterLink>
            <RouterLink to="/penerima" :class="getLinkClass('/penerima')">Penerima</RouterLink>
        </nav>
    </header>
</template>