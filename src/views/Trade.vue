<template>
  <div class="page trade">
    <NavBar />
    <div class="container">
      <div class="trade-panel">
        <div class="tabs">
          <button 
            :class="{ active: activeTab === 'buy' }" 
            @click="activeTab = 'buy'"
          >
            购买
          </button>
          <button 
            :class="{ active: activeTab === 'sell' }" 
            @click="activeTab = 'sell'"
          >
            出售
          </button>
        </div>

        <div class="trade-content">
          <div class="form-group">
            <label>选择物品</label>
            <select v-model="selectedItem">
              <option value="" disabled>请选择...</option>
              <option v-for="item in items" :key="item.id" :value="item.id">
                {{ item.name }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label>价格 (G)</label>
            <input type="number" v-model="price" placeholder="0.00">
          </div>

          <div class="form-group">
            <label>数量</label>
            <input type="number" v-model="amount" placeholder="1">
          </div>

          <div class="summary">
            <p>总计: <span>{{ total }} G</span></p>
          </div>

          <button class="button-primary full-width" @click="executeTrade">
            {{ activeTab === 'buy' ? '确认购买' : '确认出售' }}
          </button>
        </div>
      </div>

      <div class="order-book">
        <h3>市场挂单</h3>
        <div class="book-header">
          <span>价格</span>
          <span>数量</span>
          <span>时间</span>
        </div>
        <div class="book-list">
          <div class="book-item" v-for="i in 10" :key="i">
            <span :class="i % 2 === 0 ? 'text-up' : 'text-down'">{{ (100 + i * 2).toFixed(2) }}</span>
            <span>{{ i }}</span>
            <span class="time">12:0{{ i }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import NavBar from '../components/NavBar.vue'
import { fetchListings } from '../api/market'
import { createOrder } from '../api/trade'

const route = useRoute()
const activeTab = ref('buy')
const selectedItem = ref('')
const price = ref('')
const amount = ref(1)
const items = ref([])

onMounted(async () => {
  // Load items for dropdown
  try {
    const res = await fetchListings({ limit: 100 })
    items.value = res.data || []
  } catch (e) {
    console.error(e)
    items.value = [
      { id: 1, name: 'AK47 | 火蛇' },
      { id: 2, name: 'M4A4 | 咆哮' },
      { id: 3, name: 'AWP | 巨龙传说' }
    ]
  }

  // Pre-select item if passed in query
  if (route.query.itemId) {
    selectedItem.value = Number(route.query.itemId)
  }
  if (route.query.action) {
    activeTab.value = route.query.action
  }
})

const total = computed(() => {
  return (Number(price.value) * Number(amount.value)).toFixed(2)
})

const executeTrade = async () => {
  if (!selectedItem.value || !price.value || !amount.value) {
    alert('请填写完整信息')
    return
  }

  try {
    await createOrder({
      itemId: selectedItem.value,
      price: Number(price.value),
      amount: Number(amount.value),
      type: activeTab.value
    })
    alert(`${activeTab.value === 'buy' ? '购买' : '出售'} 订单已提交`)
  } catch (error) {
    console.error(error)
    alert('交易失败: ' + (error.message || '未知错误'))
  }
}
</script>

<style scoped>
.container {
  display: flex;
  gap: 20px;
  padding: 40px;
  max-width: 1200px;
  margin: 0 auto;
}

.trade-panel, .order-book {
  background: var(--panel);
  border-radius: 12px;
  padding: 20px;
}

.trade-panel {
  flex: 1;
}

.order-book {
  flex: 1;
}

.tabs {
  display: flex;
  margin-bottom: 20px;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}

.tabs button {
  flex: 1;
  background: transparent;
  border: none;
  color: var(--text-light);
  padding: 15px;
  cursor: pointer;
  font-size: 16px;
  border-bottom: 2px solid transparent;
}

.tabs button.active {
  color: var(--primary);
  border-bottom-color: var(--primary);
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: var(--text-light);
}

.form-group input, .form-group select {
  width: 100%;
  padding: 12px;
  background: rgba(0,0,0,0.2);
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 6px;
  color: var(--text);
}

.summary {
  margin: 20px 0;
  text-align: right;
  font-size: 18px;
}

.summary span {
  color: var(--primary);
  font-weight: bold;
}

.full-width {
  width: 100%;
  padding: 15px;
  font-size: 16px;
}

.book-header, .book-item {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  padding: 10px;
  border-bottom: 1px solid rgba(255,255,255,0.05);
}

.book-header {
  color: var(--text-light);
  font-size: 14px;
}

.text-up { color: var(--up); }
.text-down { color: var(--down); }
.time { color: var(--text-light); }
</style>
