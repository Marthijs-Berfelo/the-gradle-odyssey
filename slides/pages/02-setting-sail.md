---
transition: slide-up
---

# <StageIcon name="ship" :size="40" /> Setting Sail

<WaveDivider />

Multi-module build — a fleet of modules, one voyage

<br/>

Hero, Monster, and Encounter set sail as their own services, sharing one `api-spec`.

<!--
Ithaca's single module splits into four: heroes-service, monsters-service, and
encounters-service become independent Spring Boot deployables, each able to ship on its
own schedule. Turns out planning for "two services" was itself an off-by-one error —
Encounter wanted a passport of its own too. A fourth module, api-spec, holds the OpenAPI
contract all three services depend on — the shared code that lets encounters-service call
the other two to resolve the /encounters endpoint.

The catch: each module now hand-rolls its own Spring Boot, Kotlin, and OpenAPI-codegen
build setup. That duplication is this stage's problem to notice.
-->

---
transition: slide-up
---

<StageFooter icon="ship" name="Setting Sail" />

## Schematic View

<div class="flex justify-center items-center h-full">

```mermaid {scale: 0.95}
%%{init: {'theme': 'base', 'themeVariables': {
  'primaryColor': '#3d2f14',
  'primaryTextColor': '#e8dcc4',
  'primaryBorderColor': '#d4af37',
  'lineColor': '#d4af37',
  'edgeLabelBackground': '#0d1b2a',
  'fontFamily': 'Georgia, serif'
}}}%%
flowchart LR
    subgraph repo["one repo"]
        direction LR
        subgraph h["heroes-service"]
            hb["build.gradle.kts"]:::config
        end
        subgraph m["monsters-service"]
            mb["build.gradle.kts"]:::config
        end
        subgraph e["encounters-service"]
            eb["build.gradle.kts"]:::config
        end
        subgraph a["api-spec"]
            ab["build.gradle.kts"]:::config
        end
        h --> a
        m --> a
        e --> a
    end
    style repo fill:#0d1b2a,stroke:#d4af37,stroke-width:2px,stroke-dasharray:4 4
    style h fill:#3d2f14,stroke:#d4af37,stroke-width:1px
    style m fill:#3d2f14,stroke:#d4af37,stroke-width:1px
    style e fill:#3d2f14,stroke:#d4af37,stroke-width:1px
    style a fill:#3d2f14,stroke:#d4af37,stroke-width:1px
    classDef config fill:#7a1f2b,stroke:#d4af37,stroke-width:2px,color:#e8dcc4
```

</div>

---
transition: slide-down
---

<StageFooter icon="ship" name="Setting Sail" />

## Code Demo

<!--
TODO: content
-->

---
transition: slide-down
---

<StageFooter icon="ship" name="Setting Sail" />

## Pros & Cons

<br/>

<div class="grid grid-cols-2 gap-x-8 mt-4">
  <div>
    <h3 style="color: var(--odyssey-gold); font-weight: 700">Pros</h3>
    <ul>
      <li><code>heroes-service</code>, <code>monsters-service</code>, and <code>encounters-service</code> deploy independently</li>
      <li>Clear module boundaries mirror service boundaries</li>
      <li><code>api-spec</code> gives every service a single shared contract</li>
    </ul>
  </div>
  <div>
    <h3 style="color: var(--odyssey-rose); font-weight: 700">Cons</h3>
    <ul>
      <li>Spring Boot, Kotlin, and OpenAPI-codegen setup copy-pasted across 4 modules</li>
      <li>Version bumps must be repeated in every module's <code>build.gradle.kts</code></li>
      <li>No shared place to fix a build mistake — it must be fixed four times</li>
    </ul>
  </div>
</div>

---
transition: slide-left
---

<StageFooter icon="ship" name="Setting Sail" />

## Conclusion

<div class="absolute inset-0 flex items-center justify-center text-center text-xl px-20">

Independent deployability, at the cost of the same build setup — copy-pasted four times over.

</div>

<!--
Splitting into services won us independent deployability, but it cost us — the same
Spring Boot, Kotlin, and OpenAPI-codegen setup is now hand-rolled four times over,
once per module. Time to stop copy-pasting build logic.
-->
