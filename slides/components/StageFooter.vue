<script setup lang="ts">
import StageIcon from './StageIcon.vue'

defineProps<{
  icon: 'column' | 'ship' | 'eye' | 'waves' | 'laurel'
  name: string
}>()
</script>

<template>
  <div class="stage-footer abs-bl m-6 flex items-center gap-2 text-sm">
    <StageIcon :name="icon" :size="18" />
    <span>{{ name }}</span>
  </div>
</template>

<style scoped>
.stage-footer {
  color: var(--odyssey-gold, #d4af37);
  opacity: 0.6;
  letter-spacing: 0.02em;
}
</style>
