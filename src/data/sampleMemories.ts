// src/data/sampleMemories.ts
import { Memory } from "../types/memory";

export const sampleMemories: Memory[] = [
  {
    id: "mem_react_snapshot",
    title: "React State as a Snapshot — Deep Dive",
    content: "https://react.dev/learn/state-as-a-snapshot",
    why: "I need this before I start building my final project so I don't introduce race conditions in async event handlers.",
    category: "Learn",
    createdAt: Date.now() - 1000 * 60 * 60 * 5, // 5 hours ago
    updatedAt: Date.now() - 1000 * 60 * 60 * 5,
  },
  {
    id: "mem_tailwind_components",
    title: "shadcn/ui - Accessible Primitive Drawer & Combobox",
    content: "https://github.com/shadcn-ui/ui",
    why: "Steal the accessible drawer and command palette design pattern for the redesign next sprint.",
    category: "Reference",
    createdAt: Date.now() - 1000 * 60 * 60 * 24, // 1 day ago
    updatedAt: Date.now() - 1000 * 60 * 60 * 24,
  },
  {
    id: "mem_stripe_webhooks",
    title: "Stripe Subscriptions Webhook Lifecycle Guide",
    content: "https://docs.stripe.com/billing/subscriptions/webhooks",
    why: "Check exact event payloads for invoice.payment_succeeded and customer.subscription.deleted before shipping billing.",
    category: "Reference",
    createdAt: Date.now() - 1000 * 60 * 60 * 72, // 3 days ago
    updatedAt: Date.now() - 1000 * 60 * 60 * 72,
  },
];
