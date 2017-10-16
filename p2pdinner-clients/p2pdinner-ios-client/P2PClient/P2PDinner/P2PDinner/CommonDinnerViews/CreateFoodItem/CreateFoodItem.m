//
//  CreateFoodItem.m
//  P2PDinner
//
//  Created by Selvam M on 3/20/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "CreateFoodItem.h"
#import "CategoryItems.h"
#import "CategorySelectController.h"
#import "StringConstants.h"

@interface CreateFoodItem()<CategorySelectDelegate>{
    UITextField *itemTitle;
    UITextView *textViewDescription;
    UITapGestureRecognizer *tapGuester;
}
@end

@implementation CreateFoodItem
@synthesize itemDetails;

- (void)swipeRecognizer:(UISwipeGestureRecognizer *)sender {
    itemDetails.title=itemTitle.text;
    itemDetails.dinnerDescription=textViewDescription.text;
    itemDetails.dinnerCategories =itemCategoryLable.text;

    [self performSegueWithIdentifier:kAddTime sender:self];
    
}

- (void)swipGesture{
    UISwipeGestureRecognizer *recognizer = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(swipeRecognizer:)];
    recognizer.direction = UISwipeGestureRecognizerDirectionLeft;
    recognizer.delegate = self;
    [self.view addGestureRecognizer:recognizer];
    [_createFoodTable addGestureRecognizer:recognizer];
}
-(void)tapRecognizer{
    [textViewDescription resignFirstResponder];
    [itemTitle resignFirstResponder];
    itemDetails.title=itemTitle.text;
    itemDetails.dinnerDescription=textViewDescription.text;
    itemDetails.dinnerCategories =itemCategoryLable.text;
    
    
}
- (void)tapGesture{
    tapGuester = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapRecognizer)];
    tapGuester.delegate = self;
    //[self.view addGestureRecognizer:tapGuester];
    //[_createFoodTable addGestureRecognizer:tapGuester];
    //[self.view addGestureRecognizer:tapGuester];
}
- (void)viewDidLoad{
    [super viewDidLoad];
    [self tapGesture];
    
    categoryArray=[itemDetails.dinnerCategories componentsSeparatedByString:kComa_String];
        
//    _createFoodTable.sectionHeaderHeight=38;
    
    //[self navigationBarsetup];3
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardMoved:)
                                                 name:UIKeyboardDidShowNotification
                                               object:nil];
    
    
}
#pragma UISetupDinnerView
- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width, 58)];
    /* Create custom view to display section header... */
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(20, 30, tableView.frame.size.width, 18)];
    [label setTextColor:[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1]];
    [label setFont:[UIFont fontWithName:kFont_Name size:18]];
    NSString *string =kCreatFoodItem;
    
    /* Section header is in 0th index... */
    [label setText:string];
    [view addSubview:label];
    [view setBackgroundColor:[UIColor clearColor]]; //your background color...
    return view;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
    // This will create a "invisible" footer
    return 0.01f;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 58.0f;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return 3;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell;
    if (indexPath.row==0) {
        cell= (UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:kTitleCell];
        itemTitle=(UITextField *)[cell viewWithTag:111];
        if ([itemDetails.title length]>0) {
            itemTitle.text=itemDetails.title;
        }
        [(TitleTableViewCell*)cell initialSetUpCell];
        [(TitleTableViewCell*)cell setItemDetails:itemDetails];
        [cell.contentView addGestureRecognizer:tapGuester];
        
    }
    if (indexPath.row==1) {
        cell= [tableView dequeueReusableCellWithIdentifier:kcategoryCell];
        itemCategoryLable=(UILabel *)[cell viewWithTag:111];
        if ([itemDetails.dinnerCategories length]<=0) {
            if ([itemDetails.dinnerCategories length]>0) {
                itemCategoryLable.text=itemDetails.dinnerCategories;
            }else
                itemCategoryLable.text=kSelect_Category;
        }
        else{
            itemCategoryLable.text=[self createCategoryStringFormArray];
        }
        
    }
    else if (indexPath.row==2) {
        cell= [tableView dequeueReusableCellWithIdentifier:kDescriptionCell];
       textViewDescription=(UITextView *)[cell viewWithTag:111];
        [(DescriptionTableViewCell *)cell initialSetUpCell];
        [cell.contentView addGestureRecognizer:tapGuester];
    }
    
    cell.selectionStyle =UITableViewCellSelectionStyleNone;
    
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row==2) {
        return 238;
    }
    
    return 79;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
#warning need to set selected item
    if (indexPath.row==1) {
       // [self tapGesture];
        CategorySelectController *vc = [self.storyboard instantiateViewControllerWithIdentifier:kCategorySelectController];
        vc.delegate=self;
        [self presentViewController:vc animated:YES completion:nil];
        
    }
}
//SelectedCategory
- (NSString *)getSelectedCategory:(NSArray *)selectedArray{
    NSMutableString *selectedCategory=[NSMutableString stringWithString:kSelect_Category];
    if (0<[selectedArray count]) {
        selectedCategory=[[NSMutableString alloc]init];
    }
    
    
    for (int i=0; i<[selectedArray count]; i++) {
        
        CategoryItems *categoryitem=(CategoryItems *)[selectedArray objectAtIndex:i];
        if ([categoryitem isEqual:[selectedArray lastObject]]) {
            [selectedCategory appendFormat:kSrtingPatten,categoryitem.name];
        }
        else
            [selectedCategory appendFormat:kAppendWithComaString,categoryitem.name];
    }
    
    
    return selectedCategory;
}

- (void)selectedCategors:(NSArray *)selectedArray{
    categoryArray=selectedArray;
    itemDetails.dinnerCategories=[self getSelectedCategory:selectedArray];
    itemCategoryLable.text=[self getSelectedCategory:selectedArray];
    
    [_createFoodTable rectForRowAtIndexPath:0];
}

- (NSString *)createCategoryStringFormArray{
    NSString *returnString=kEmpty_String;
    for (CategoryItems *items in categoryArray) {
        if ([returnString length]>1) {
            returnString=[NSString stringWithFormat:k2StringAppendFormartWithComa,returnString,([items isKindOfClass:[CategoryItems class]])?items.name:items];
        }
        else{
            returnString = ([items isKindOfClass:[CategoryItems class]])?items.name:(NSString *)items;
        }
        
    }
    return returnString;
}

- (BOOL)textViewShouldBeginEditing:(UITextView *)textView{
    _createFoodTable.scrollEnabled = NO;
    [_createFoodTable setContentOffset:CGPointMake(0, 152) animated:YES];
    return true;
}

- (BOOL)textViewShouldEndEditing:(UITextView *)textView{
    _createFoodTable.scrollEnabled = YES;
    [_createFoodTable setContentOffset:CGPointMake(0, 0) animated:YES];
    itemDetails.dinnerDescription=textView.text;
    return true;
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range
 replacementText:(NSString *)text
{
    
    if ([text isEqualToString:kNextLaine]) {
        CGPoint point=_createFoodTable.contentOffset;
        if (point.y<190) {
            [_createFoodTable setContentOffset:CGPointMake(point.x, point.y+10) animated:YES];
        }
        
        // Return FALSE so that the final '\n' character doesn't get added
        return YES;
    }
    // For any other character return TRUE so that the text gets added to the view
    return YES;
}

- (void) keyboardMoved:(NSNotification*)notify {
    
    // Get values
    CGRect endFrame;
    [[[notify userInfo] valueForKey:UIKeyboardFrameEndUserInfoKey] getValue:&endFrame];
    endFrame = [self.view convertRect:endFrame fromView:nil];
    
    // Add padding
    int inset = self.view.bounds.size.height - endFrame.origin.y;
    _createFoodTable.contentInset = UIEdgeInsetsMake(0, 0, inset < 0 ? 0 : inset, 0);
    _createFoodTable.scrollIndicatorInsets = _createFoodTable.contentInset;
}

- (IBAction)textViewEndEditing:(id)sender{
    UITextField *textField=(UITextField *)sender;
    itemDetails.title=textField.text;
    
}
@end
