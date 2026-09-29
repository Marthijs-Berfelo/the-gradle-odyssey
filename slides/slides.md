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
transition: slide-left
---

<div class="flex flex-col items-center justify-center h-full gap-4">
  <img
    src="/images/helmsman.jpg"
    alt="Marthijs Berfelo as a Greek helmsman"
    class="rounded-full object-cover"
    style="width: 180px; height: 180px; border: 3px solid var(--odyssey-gold); box-shadow: 0 0 0 6px rgba(212, 175, 55, 0.15)"
  />
  <div class="text-center">
    <div class="text-2xl italic" style="font-family: 'Cormorant Garamond', Georgia, serif; color: var(--odyssey-parchment)">Marthijs Berfelo</div>
    <div class="text-lg" style="color: var(--odyssey-gold)">Software Engineer — your helmsman for this voyage</div>
    <div class="text-base mt-2 opacity-80">Charting safer courses through multi-module Gradle builds</div>
  </div>
  <div class="flex flex-col items-center gap-1 text-base mt-2">
    <a href="https://github.com/Marthijs-Berfelo" target="_blank" class="flex items-center gap-2" style="color: var(--odyssey-gold)">
      <carbon:logo-github /> github.com/Marthijs-Berfelo
    </a>
    <a href="https://www.linkedin.com/in/marthijs-berfelo-b393aa33/" target="_blank" class="flex items-center gap-2" style="color: var(--odyssey-gold)">
      <carbon:logo-linkedin /> linkedin.com/in/marthijs-berfelo
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
transition: slide-left
class: text-center
---

# The Journey, Complete

<WaveDivider />

Five stages, one build — from a single module to a plugin the world can reuse.

<div class="flex justify-center mt-4" style="transform: scale(0.6); transform-origin: top center;">
  <JourneyTrail />
</div>

<!--
Ithaca: one module, one build — simple, but Hero and Monster couldn't ship independently.

Setting Sail: split into services won independent deployability, at the cost of
copy-pasted build setup.

The Cyclops's Cave: buildSrc deduplicated that setup — but stayed trapped in this repo.

The Sirens: a version catalog aligned every dependency — but build logic was still landlocked.

Ithaca, Regained: buildSrc and the catalog both became published artifacts — true reuse,
no copying, no drift.

That's the odyssey: from a build that couldn't be shared, to one that can sail anywhere.
-->

---
layout: center
class: text-center
---

# Thank You

<WaveDivider />

<div class="flex flex-col items-center justify-center gap-3 mt-6">
  <img
    src="/images/github-qr.svg"
    alt="QR code linking to the GitHub repository"
    style="width: 192px; height: 192px; border: 3px solid var(--odyssey-gold); border-radius: 12px; background: var(--odyssey-gold); padding: 10px; box-shadow: 0 0 0 6px rgba(212, 175, 55, 0.15), 0 8px 24px rgba(0, 0, 0, 0.35)"
  />
  <a href="https://github.com/Marthijs-Berfelo/the-gradle-odyssey" target="_blank" class="flex items-center gap-2 text-lg" style="color: var(--odyssey-gold)">
    <carbon:logo-github /> github.com/Marthijs-Berfelo/the-gradle-odyssey
  </a>
</div>
