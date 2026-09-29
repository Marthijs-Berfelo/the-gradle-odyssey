<script setup lang="ts">
import StageIcon from './StageIcon.vue'

const stages = [
  { icon: 'column', name: 'Ithaca' },
  { icon: 'ship', name: 'Setting Sail' },
  { icon: 'eye', name: "The Cyclops's Cave" },
  { icon: 'waves', name: 'The Sirens' },
  { icon: 'laurel', name: 'Ithaca, Regained' },
] as const
</script>

<template>
  <div class="journey-trail">
    <svg class="journey-trail-path" viewBox="0 0 60 420" preserveAspectRatio="none">
      <path
        d="M30 20 Q5 90 35 140 T25 260 T40 340 T20 400"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-dasharray="1 10"
        stroke-linecap="round"
      />
    </svg>
    <div class="journey-trail-stops">
      <div v-for="stage in stages" :key="stage.name" class="journey-trail-stop">
        <StageIcon :name="stage.icon" :size="40" />
        <span class="journey-trail-name">{{ stage.name }}</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.journey-trail {
  display: flex;
  align-items: stretch;
  gap: 1.5rem;
  margin-top: 1.5rem;
}

.journey-trail-path {
  flex: none;
  width: 60px;
  height: 420px;
  color: var(--odyssey-gold, #d4af37);
  opacity: 0.7;
}

.journey-trail-stops {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 0.5rem 0;
}

.journey-trail-stop {
  display: flex;
  align-items: center;
  gap: 1rem;
  height: 84px;
}

.journey-trail-name {
  font-size: 1.1rem;
  color: var(--odyssey-parchment, #e8dcc4);
}
</style>
