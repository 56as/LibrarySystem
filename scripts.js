// scripts.js

document.addEventListener('DOMContentLoaded', function() {
    // 页面加载完成后获取图书列表
    fetchBooks();
    // 为所有表单添加事件监听器
    attachEventListeners();
});

// 为所有表单添加事件监听器
function attachEventListeners() {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => form.addEventListener('submit', handleFormSubmit));
}

// 处理表单提交事件
function handleFormSubmit(event) {
    event.preventDefault(); // 阻止表单的默认提交行为
    const form = event.target;
    const action = form.getAttribute('data-action'); // 获取表单对应的操作
    const url = `/api/${action}`; // 构建请求的URL

    const formData = new FormData(form); // 创建FormData对象，用于封装表单数据
    fetch(url, {
        method: 'POST', // 所有操作均使用POST方法
        body: formData,
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('网络响应不正常');
        }
        return response.json(); // 解析JSON数据
    })
    .then(data => {
        console.log('成功:', data);
        alert(`${action}成功！`);
        form.reset(); // 清空表单
        if (action === 'fetchBooks') {
            fetchBooks(); // 重新加载图书列表
        } else if (action === 'displayRecords') {
            displayRecords(data); // 显示借阅记录
        }
    })
    .catch(error => {
        console.error('错误:', error);
        alert('处理失败。');
    });
}

// 获取图书列表并显示
function fetchBooks() {
    fetch('/api/books')
        .then(response => response.json())
        .then(books => displayBooks(books))
        .catch(error => console.error('获取图书列表出错:', error));
}

// 显示图书列表
function displayBooks(books) {
    const booksContainer = document.getElementById('availableBooks');
    booksContainer.innerHTML = ''; // 清空现有列表
    books.forEach(book => {
        const li = document.createElement('li');
        li.textContent = `${book.title} by ${book.author}`; // 假设书籍对象有title和author属性
        li.setAttribute('data-book-id', book.id); // 假设书籍对象有id属性
        booksContainer.appendChild(li);
    });
}

// 显示借阅记录
function displayRecords(records) {
    const recordsContainer = document.getElementById('borrowingHistory');
    recordsContainer.innerHTML = ''; // 清空现有列表
    records.forEach(record => {
        const li = document.createElement('li');
        li.textContent = `${record.bookTitle} by ${record.author} - ${record.date}`; // 假设记录对象有bookTitle, author和date属性
        recordsContainer.appendChild(li);
    });
}

// 显示搜索结果
function displaySearchResults(results) {
    const searchResultsContainer = document.getElementById('searchResults');
    searchResultsContainer.innerHTML = ''; // 清空现有搜索结果
    results.forEach(result => {
        const div = document.createElement('div');
        div.textContent = `${result.title} by ${result.author}`; // 假设搜索结果对象有title和author属性
        searchResultsContainer.appendChild(div);
    });
}