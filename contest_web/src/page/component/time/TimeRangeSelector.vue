<template>
  <t-space direction="vertical">
    <t-date-range-picker @change="$emit('change',range)" v-model="range" enable-time-picker />
  </t-space>
</template>

<script setup>

import { ref } from 'vue';
const range = ref([]);

</script>
