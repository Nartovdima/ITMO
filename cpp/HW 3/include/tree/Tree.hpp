#ifndef TREE_HPP
#define TREE_HPP

#include <cstddef>
#include <cstdint>
#include <iostream>
#include <vector>

class Splay {
private:
    struct Node {
        int value;
        Node* left;
        Node* right;
        Node* parent;

        Node(int value, Node* left, Node* right, Node* parent) {
            this->value  = value;
            this->left   = left;
            this->right  = right;
            this->parent = parent;
        }

        ~Node();
    };

    std::size_t tree_size = 0;
    mutable Node* root    = nullptr;

    void rotate_left(Node* x) const noexcept;
    void rotate_right(Node* x) const noexcept;

    bool zig(Node* x) const noexcept;
    bool zig_zig(Node* x) const noexcept;
    bool zig_zag(Node* x) const noexcept;

    void splay(Node* x) const noexcept;

    void dump_tree(Node* node, std::vector<int>& tree) const noexcept;

    Node* find(int value) const noexcept;

    Node* insert(Node*& node, Node*& prev_node, int value);

public:
    [[nodiscard]] bool contains(int value) const noexcept;
    bool insert(int value);
    bool remove(int value);

    [[nodiscard]] std::size_t size() const noexcept;
    [[nodiscard]] bool empty() const noexcept;

    [[nodiscard]] std::vector<int> values() const noexcept;

    ~Splay();
};

#endif
