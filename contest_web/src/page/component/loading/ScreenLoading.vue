<template>
  <t-space direction="vertical">
    <t-loading :loading="loading" text="试卷分发中..." fullscreen />
  </t-space>
</template>
<script setup>
import {toRef} from 'vue';
import {defineProps} from "vue";

const props = defineProps({
  loading: {
    type: Boolean,
    required: true
  }
})

const loading = toRef(props,'loading');

</script>
