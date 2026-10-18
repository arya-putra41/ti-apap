<script setup lang="ts">
import { RouterView } from 'vue-router'
import { Toaster } from 'vue-sonner'
import VNavbar from './components/layout/VNavbar.vue';
</script>

<template>
  <Toaster position="top-center" rich-colors/>
  <VNavbar/>
  <RouterView/>
</template>