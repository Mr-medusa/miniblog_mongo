export default {
    cards: new Map(),
    maxSize: 50,
    putCard(blog) {
        this.cards.set(blog.id, blog);
        if (this.cards.size >= this.maxSize)
            this.cards.delete(this.cards.keys().next().value);
    },
    getCard(id) {
        return this.cards.get(id);
    },
    deleteCard(id){
        this.cards.delete(id);
    }
}