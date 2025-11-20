<template>
  <div class="notification" v-if="message">{{ message }}</div>
</template>

<script>
export default {
  name: 'Notification',
  props: { message: String }
}
</script>

<style scoped>
.notification {
  background: var(--panel);
  color: var(--text);
  padding: 12px 20px;
  border: 1px solid var(--primary);
  border-radius: 6px;
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 1000;
  box-shadow: 0 4px 12px rgba(0,0,0,0.3);
}
</style>
