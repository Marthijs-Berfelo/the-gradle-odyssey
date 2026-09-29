<script setup lang="ts">
withDefaults(defineProps<{
  name: 'column' | 'ship' | 'eye' | 'waves' | 'laurel'
  size?: number
}>(), {
  size: 64,
})
</script>

<template>
  <svg
    :width="size"
    :height="size"
    viewBox="0 0 100 100"
    class="stage-icon"
  >
    <g v-if="name === 'column'" fill="none" stroke="currentColor" stroke-width="4" stroke-linecap="round" stroke-linejoin="round">
      <line x1="25" y1="15" x2="75" y2="15" />
      <line x1="30" y1="22" x2="70" y2="22" />
      <rect x="35" y="22" width="30" height="55" />
      <line x1="42" y1="22" x2="42" y2="77" />
      <line x1="50" y1="22" x2="50" y2="77" />
      <line x1="58" y1="22" x2="58" y2="77" />
      <line x1="25" y1="85" x2="75" y2="85" />
      <line x1="20" y1="90" x2="80" y2="90" />
    </g>
    <g v-else-if="name === 'ship'" fill="none" stroke="currentColor" stroke-width="4" stroke-linecap="round" stroke-linejoin="round">
      <path d="M15 70 Q50 85 85 70 L78 78 Q50 88 22 78 Z" fill="currentColor" stroke="none" />
      <line x1="50" y1="70" x2="50" y2="20" />
      <path d="M50 22 L50 55 L78 55 Z" />
    </g>
    <g v-else-if="name === 'eye'" fill="none" stroke="currentColor" stroke-width="4" stroke-linecap="round" stroke-linejoin="round">
      <path d="M15 50 Q50 20 85 50 Q50 80 15 50 Z" />
      <circle cx="50" cy="50" r="12" fill="currentColor" stroke="none" />
    </g>
    <g v-else-if="name === 'waves'" fill="none" stroke="currentColor" stroke-width="5" stroke-linecap="round">
      <path d="M15 35 Q30 20 50 35 Q70 50 85 35" />
      <path d="M15 55 Q30 40 50 55 Q70 70 85 55" />
      <path d="M15 75 Q30 60 50 75 Q70 90 85 75" />
    </g>
    <g v-else-if="name === 'laurel'" fill="none" stroke="currentColor" stroke-width="4" stroke-linecap="round">
      <path d="M50 85 Q30 70 25 45 Q22 25 35 15" />
      <path d="M50 85 Q70 70 75 45 Q78 25 65 15" />
      <line x1="30" y1="60" x2="20" y2="55" />
      <line x1="27" y1="45" x2="16" y2="42" />
      <line x1="26" y1="30" x2="15" y2="30" />
      <line x1="70" y1="60" x2="80" y2="55" />
      <line x1="73" y1="45" x2="84" y2="42" />
      <line x1="74" y1="30" x2="85" y2="30" />
    </g>
  </svg>
</template>

<style scoped>
.stage-icon {
  color: var(--odyssey-gold, #d4af37);
  vertical-align: middle;
}
</style>
