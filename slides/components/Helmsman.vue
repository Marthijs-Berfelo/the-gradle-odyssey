<script setup lang="ts">
withDefaults(defineProps<{
  size?: number
}>(), {
  size: 140,
})
</script>

<template>
  <svg
    :width="size"
    :height="size"
    viewBox="0 0 120 120"
    class="helmsman-portrait"
  >
    <circle cx="60" cy="60" r="56" fill="none" stroke="currentColor" stroke-width="2" opacity="0.5" />
    <circle cx="60" cy="60" r="50" fill="none" stroke="currentColor" stroke-width="1" opacity="0.3" />

    <g stroke="currentColor" stroke-width="3" fill="none" stroke-linecap="round">
      <circle cx="60" cy="72" r="30" />
      <circle cx="60" cy="72" r="7" fill="currentColor" stroke="none" />
      <line x1="60" y1="42" x2="60" y2="34" />
      <line x1="60" y1="102" x2="60" y2="110" />
      <line x1="30" y1="72" x2="22" y2="72" />
      <line x1="90" y1="72" x2="98" y2="72" />
      <line x1="39" y1="51" x2="33" y2="45" />
      <line x1="81" y1="51" x2="87" y2="45" />
      <line x1="39" y1="93" x2="33" y2="99" />
      <line x1="81" y1="93" x2="87" y2="99" />
    </g>

    <g fill="currentColor" stroke="none">
      <circle cx="60" cy="26" r="9" />
      <path d="M45 58 Q45 36 60 36 Q75 36 75 58 L75 64 Q60 54 45 64 Z" />
    </g>
  </svg>
</template>

<style scoped>
.helmsman-portrait {
  color: var(--odyssey-gold, #d4af37);
  vertical-align: middle;
}
</style>
