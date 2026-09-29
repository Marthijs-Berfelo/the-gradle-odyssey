---
transition: slide-up
---

# <StageIcon name="ship" :size="40" /> Setting Sail

<WaveDivider />

Multi-module build — a fleet of modules, one voyage

<!--
TODO: content
-->

---
transition: slide-up
---

## Schematic View

<div class="flex justify-center items-center h-full">

```mermaid {theme: 'dark', scale: 1.1}
flowchart LR
    heroes["heroes-service"] --> apispec["api-spec"]
    monsters["monsters-service"] --> apispec
```

</div>

---
transition: slide-down
---

## Code Demo

<!--
TODO: content
-->

---
transition: slide-down
---

## Pros & Cons

<div class="grid grid-cols-2 gap-x-8 mt-4">
  <div>
    <h3 style="color: var(--odyssey-gold)">Pros</h3>
    <ul>
      <li><code>heroes-service</code> and <code>monsters-service</code> deploy independently</li>
      <li>Clear module boundaries mirror service boundaries</li>
      <li><code>api-spec</code> gives each service a single shared contract</li>
    </ul>
  </div>
  <div>
    <h3 style="color: var(--odyssey-wine)">Cons</h3>
    <ul>
      <li>Spring Boot, Kotlin, and OpenAPI-codegen setup copy-pasted across 3 modules</li>
      <li>Version bumps must be repeated in every module's <code>build.gradle.kts</code></li>
      <li>No shared place to fix a build mistake — it must be fixed three times</li>
    </ul>
  </div>
</div>

---
transition: slide-left
---

## Conclusion

Splitting into services won us independent deployability, but it cost us — the same Spring Boot, Kotlin, and OpenAPI-codegen setup is now hand-rolled three times over. Time to stop copy-pasting build logic.
