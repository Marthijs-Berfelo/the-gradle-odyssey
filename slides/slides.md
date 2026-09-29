---
theme: seriph
title: The Gradle Odyssey
favicon: /favicon.svg
info: |
  ## The Gradle Odyssey
  A journey through Gradle build reuse — version catalogs, BOMs, buildSrc, and shared plugins.
fonts:
  serif: 'Cormorant Garamond'
  provider: google
class: text-center
transition: slide-left
---

# The Gradle Odyssey

<WaveDivider />

A journey through reusing Gradle build configuration

<div class="abs-br m-6 text-xl">
  <a href="https://github.com/Marthijs-Berfelo/the-gradle-odyssey" target="_blank" class="slidev-icon-btn">
    <carbon:logo-github />
  </a>
</div>

---
transition: slide-down
---

<div class="flex flex-col items-center justify-center h-full gap-4">
  <Helmsman :size="140" />
  <div class="text-center">
    <div class="text-2xl italic" style="font-family: 'Cormorant Garamond', Georgia, serif; color: var(--odyssey-parchment)">Marthijs Berfelo</div>
    <div class="text-lg" style="color: var(--odyssey-gold)">Software Engineer — your helmsman for this voyage</div>
    <div class="text-base mt-2 opacity-80">Charting safer courses through multi-module Gradle builds</div>
  </div>
  <div class="flex gap-4 text-xl mt-2">
    <a href="https://github.com/Marthijs-Berfelo" target="_blank" class="slidev-icon-btn">
      <carbon:logo-github />
    </a>
    <a href="https://www.linkedin.com/in/marthijs-berfelo-b393aa33/" target="_blank" class="slidev-icon-btn">
      <carbon:logo-linkedin />
    </a>
  </div>
</div>

---
transition: fade-out
---

# The Journey

<JourneyTrail />

<!--
Odysseus had no idea how long the way home would be —
if he had, he might never have set sail.
-->

---
src: ./pages/01-ithaca.md
---

---
src: ./pages/02-setting-sail.md
---

---
src: ./pages/03-cyclops-cave.md
---

---
src: ./pages/04-sirens.md
---

---
src: ./pages/05-ithaca-regained.md
---

---
layout: center
class: text-center
---

# Thank You

<WaveDivider />

<div class="abs-br m-6 text-xl">
  <a href="https://github.com/Marthijs-Berfelo/the-gradle-odyssey" target="_blank" class="slidev-icon-btn">
    <carbon:logo-github />
  </a>
</div>
