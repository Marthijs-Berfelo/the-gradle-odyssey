---
transition: slide-up
---

# <StageIcon name="eye" :size="40" /> The Cyclops's Cave

<WaveDivider />

`buildSrc` — powerful, but trapped on one island

One `buildSrc` convention plugin replaces three copies of build boilerplate.

<!--
The three modules from Setting Sail each hand-rolled their own Spring Boot, Kotlin, and
OpenAPI-codegen setup. buildSrc lets us pull that duplicated logic into one convention
plugin, applied by heroes-service, monsters-service, and api-spec alike — a single source
of truth for this repo's build logic.

The catch: buildSrc only exists inside this repo. Like the Cyclops's cave, it's powerful
but sealed off — nothing inside it can be reused by any other project without copying the
whole thing over again.
-->

---
transition: slide-up
---

## Schematic View

<div class="flex justify-center items-center h-full">

```mermaid {theme: 'dark', scale: 0.85}
flowchart LR
    heroes["heroes-service"] --> apispec["api-spec"]
    monsters["monsters-service"] --> apispec
    buildSrc["buildSrc"] -.-> heroes
    buildSrc -.-> monsters
    buildSrc -.-> apispec
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
      <li>Build logic deduplicated into <code>buildSrc</code> convention plugins</li>
      <li>One place to fix or evolve the Spring Boot/Kotlin conventions</li>
      <li>Modules apply a single plugin id instead of hand-rolled blocks</li>
    </ul>
  </div>
  <div>
    <h3 style="color: var(--odyssey-wine)">Cons</h3>
    <ul>
      <li><code>buildSrc</code> is trapped inside this repo — no other project can reuse it</li>
      <li>Reusing it elsewhere means copy-pasting <code>buildSrc</code> itself</li>
      <li>Any change inside <code>buildSrc</code> invalidates the whole build's configuration cache</li>
    </ul>
  </div>
</div>

---
transition: slide-left
---

## Conclusion

<div class="flex justify-center items-center h-full text-center text-xl">

<code>buildSrc</code> is a cave, not a harbor — nothing inside it can leave this repo.

</div>

<!--
buildSrc deduplicated the build logic beautifully — one source of truth, for this repo.
But that's exactly its limit: buildSrc is a cave, not a harbor. Nothing inside it can leave,
so any other project wanting this build logic has to copy the whole cave.
-->
